/*
 * Decompiled with CFR 0.152.
 */
package org.openysm.client.upload;

import org.openysm.network.NetworkHandler;
import org.openysm.network.message.C2SModelUploadChunkPacket;
import org.openysm.network.message.C2SModelUploadFinishPacket;
import org.openysm.network.message.C2SModelUploadStartPacket;
import org.openysm.util.DigestUtil;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ModelUploadSession {
    private static final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList();
    private static volatile ModelUploadSession instance;
    private static volatile boolean serverLimitsKnown;
    private static volatile int lastMaxTotalBytes;
    private static volatile int lastChunksPerTick;
    private final String modelId;
    private final byte[] data;
    private final String sha256;
    private volatile State state = State.STARTING;
    private volatile long uploadId = 0L;
    private volatile int chunkSize = 32000;
    private volatile int chunksPerTick = 4;
    private volatile int nextOffset = 0;
    private volatile String message = "";

    private ModelUploadSession(String modelId, byte[] data) {
        this.modelId = modelId;
        this.data = data;
        this.sha256 = DigestUtil.sha256Hex(data);
    }

    public static ModelUploadSession getInstance() {
        return instance;
    }

    public static synchronized String start(String modelId, byte[] data) {
        ModelUploadSession session;
        if (instance != null && !instance.isTerminal()) {
            return "Upload already in progress";
        }
        if (data.length == 0) {
            return "Empty file";
        }
        if (serverLimitsKnown && data.length > lastMaxTotalBytes) {
            return "File exceeds server limit (" + ModelUploadSession.formatBytes(lastMaxTotalBytes) + ")";
        }
        if (!ModelUploadSession.isYsmFile(data)) {
            return "Invalid file type!";
        }
        instance = session = new ModelUploadSession(modelId, data);
        ModelUploadSession.notifyListeners();
        NetworkHandler.sendToServer(new C2SModelUploadStartPacket(modelId, data.length, session.sha256));
        return null;
    }

    public static boolean hasServerLimits() {
        return serverLimitsKnown;
    }

    public static int getLastMaxTotalBytes() {
        return lastMaxTotalBytes;
    }

    public static int getLastChunksPerTick() {
        return lastChunksPerTick;
    }

    public static String formatBytes(int bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 0x100000) {
            return String.format("%.1f KB", (double)bytes / 1024.0);
        }
        return String.format("%.2f MB", (double)bytes / 1048576.0);
    }

    public static synchronized void clearIfTerminal() {
        if (instance != null && instance.isTerminal()) {
            instance = null;
            ModelUploadSession.notifyListeners();
        }
    }

    public static void addListener(Listener l) {
        listeners.add(l);
    }

    public static void removeListener(Listener l) {
        listeners.remove(l);
    }

    public static synchronized void onStartAck(long uploadId, byte status, int chunkSize, int maxTotalBytes, int chunksPerTick, String message) {
        if (maxTotalBytes > 0) {
            lastMaxTotalBytes = maxTotalBytes;
        }
        if (chunksPerTick > 0) {
            lastChunksPerTick = chunksPerTick;
        }
        serverLimitsKnown = true;
        ModelUploadSession s = instance;
        if (s == null || s.state != State.STARTING) {
            return;
        }
        if (status != 0) {
            s.fail(ModelUploadSession.getRequestErrorText(status) + (String)(message.isEmpty() ? "" : ": " + message));
            return;
        }
        s.uploadId = uploadId;
        s.chunkSize = Math.max(1, chunkSize);
        s.chunksPerTick = Math.max(1, chunksPerTick);
        s.state = State.UPLOADING;
        s.message = "Uploading\u2026";
        ModelUploadSession.notifyListeners();
    }

    public static synchronized void onResult(long uploadId, byte status, String modelId, long h1, long h2, String message) {
        ModelUploadSession s = instance;
        if (s == null || s.uploadId != uploadId) {
            return;
        }
        if (status == 0) {
            s.state = State.COMPLETED;
            s.message = "Uploaded as " + modelId;
        } else {
            s.fail(ModelUploadSession.getResponseErrorText(status) + (String)(message.isEmpty() ? "" : ": " + message));
        }
        ModelUploadSession.notifyListeners();
    }

    public static void tickCurrent() {
        ModelUploadSession s = instance;
        if (s == null) {
            return;
        }
        s.tick();
    }

    private static void notifyListeners() {
        ModelUploadSession s = instance;
        for (Listener l : listeners) {
            l.onSessionUpdate(s);
        }
    }

    private static boolean isYsmFile(byte[] data) {
        byte[] ysmHeader = new byte[]{-17, -69, -65, 89, 83, 71, 80};
        if (data.length < ysmHeader.length) {
            return false;
        }
        for (int i = 0; i < ysmHeader.length; ++i) {
            if (data[i] == ysmHeader[i]) continue;
            return false;
        }
        return true;
    }

    private static String getRequestErrorText(byte status) {
        return switch (status) {
            case 1 -> "Model ID already exists";
            case 2 -> "File exceeds server limit";
            case 3 -> "No upload permission";
            case 4 -> "Server busy, try again later";
            case 5 -> "Invalid model ID or hash";
            case 6 -> "Uploads disabled on server";
            default -> "error: " + status;
        };
    }

    private static String getResponseErrorText(byte status) {
        return switch (status) {
            case 1 -> "Hash mismatch";
            case 2 -> "Server failed to parse model";
            case 3 -> "Server storage error";
            case 4 -> "Session expired";
            case 5 -> "Incomplete upload";
            case 6 -> "Server rejected write";
            default -> "error: " + status;
        };
    }

    private synchronized void tick() {
        if (this.state != State.UPLOADING) {
            return;
        }
        int budget = Math.max(1, this.chunksPerTick);
        for (int i = 0; i < budget && this.nextOffset < this.data.length; ++i) {
            int end = Math.min(this.nextOffset + this.chunkSize, this.data.length);
            byte[] slice = Arrays.copyOfRange(this.data, this.nextOffset, end);
            NetworkHandler.sendToServer(new C2SModelUploadChunkPacket(this.uploadId, this.nextOffset, slice));
            this.nextOffset = end;
        }
        if (this.nextOffset >= this.data.length) {
            this.state = State.FINISHING;
            this.message = "Verifying\u2026";
            NetworkHandler.sendToServer(new C2SModelUploadFinishPacket(this.uploadId));
        }
        ModelUploadSession.notifyListeners();
    }

    private void fail(String reason) {
        this.state = State.FAILED;
        this.message = reason;
    }

    public boolean isTerminal() {
        return this.state == State.COMPLETED || this.state == State.FAILED;
    }

    public State getState() {
        return this.state;
    }

    public String getModelId() {
        return this.modelId;
    }

    public int getTotalBytes() {
        return this.data.length;
    }

    public int getSentBytes() {
        return Math.min(this.nextOffset, this.data.length);
    }

    public String getMessage() {
        return this.message;
    }

    public float getProgress() {
        if (this.data.length == 0) {
            return 1.0f;
        }
        if (this.state == State.COMPLETED) {
            return 1.0f;
        }
        return (float)this.getSentBytes() / (float)this.data.length;
    }

    static {
        serverLimitsKnown = false;
        lastMaxTotalBytes = 0x1000000;
        lastChunksPerTick = 4;
    }

    public static enum State {
        STARTING,
        UPLOADING,
        FINISHING,
        COMPLETED,
        FAILED;

    }

    public static interface Listener {
        public void onSessionUpdate(ModelUploadSession var1);
    }
}

