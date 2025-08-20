package jp.co.cyberagent.android.gpuimage.sample.utils;

public abstract class CameraLoader {

    protected OnPreviewFrameListener onPreviewFrame;

    public abstract void onResume(int width, int height);

    public abstract void onPause();

    public abstract void switchCamera();

    public abstract int getCameraOrientation();

    public abstract boolean hasMultipleCamera();

    public void setOnPreviewFrameListener(OnPreviewFrameListener onPreviewFrame) {
        this.onPreviewFrame = onPreviewFrame;
    }

    public interface OnPreviewFrameListener {
        void onPreviewFrame(byte[] data, int width, int height);
    }
}