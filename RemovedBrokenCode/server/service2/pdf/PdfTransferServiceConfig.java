package ru.imagebook.server.service2.pdf;

public class PdfTransferServiceConfig {
    private int delayMs;
    private int threads;

    public int getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(int delayMs) {
        this.delayMs = delayMs;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}

