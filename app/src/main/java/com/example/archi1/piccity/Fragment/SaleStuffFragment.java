package com.example.archi1.piccity.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.archi1.piccity.Activity.CameraImagePriview;
import com.example.archi1.piccity.Constant.Utils;
import com.example.archi1.piccity.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by archi1 on 11/29/2016.
 */

public class SaleStuffFragment extends android.support.v4.app.Fragment implements SurfaceHolder.Callback {

    public Camera camera;
    public SurfaceView surfaceView;
    public SurfaceHolder surfaceHolder;
    public boolean previewing = false, flash = true;
    public LayoutInflater controlInflater = null;
    public int currentCameraId = 0;
    public ImageView camera_changeview, camera_flash, camera_capture_image, gallery_image;
    public Camera.PictureCallback jpegCallback;
    public int i = 1;
    public View rootView;
    public Utils utils;
    private int RESULT_LOAD_IMG = 201;
    private String isRoyality="SaleStuff";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_sale_stuff, container, false);
        utils = new Utils(getActivity());

        init();

        Bundle bundle = getArguments();
        if (bundle != null) {
            isRoyality = bundle.getString("Royalty");

        }
        //((Activity) getActivity()).setTitle(R.string.camera);

        return rootView;
    }


    @Override
    public void onResume() {
        //getActivity().setTitle(R.string.art_list);
        super.onResume();
        //((Activity) getActivity()).setTitle(R.string.camera);

    }

    private void init() {
        gallery_image = (ImageView) rootView.findViewById(R.id.fragment_camera_gallery);
        surfaceView = (SurfaceView) rootView.findViewById(R.id.camerapreview);
        /*surfaceView.setZOrderOnTop(true);
        SurfaceHolder sfhtrackHolder = surfaceView.getHolder();
        sfhtrackHolder.setFormat(PixelFormat.TRANSPARENT);*/
        camera_changeview = (ImageView) rootView.findViewById(R.id.camera_changeview);
        camera_flash = (ImageView) rootView.findViewById(R.id.camera_flash);
        camera_capture_image = (ImageView) rootView.findViewById(R.id.camera_capture_image);
        surfaceHolder = surfaceView.getHolder();


        surfaceHolder.addCallback(this);


        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                String path = "";

                try {

                    if (utils.isExternalStorageAvailable()) {
                        File file = new File("/sdcard/" + getActivity().getString(R.string.app_name));
                        if (file.exists()) {
                            path = String.format(
                                    "/sdcard/" + getActivity().getString(R.string.app_name) + "/%d.jpg", System.currentTimeMillis());
                            outStream = new FileOutputStream(path);
                            outStream.write(data);
                            outStream.close();
                        } else {
                            if (file.mkdir()) {
                                path = String.format(
                                        "/sdcard/" + getActivity().getString(R.string.app_name) + "/%d.jpg", System.currentTimeMillis());
                                outStream = new FileOutputStream(path);
                                outStream.write(data);
                                outStream.close();
                            } else {
                                Toast.makeText(getActivity(), getActivity().getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        File file = new File("/" + getActivity().getString(R.string.app_name));
                        if (file.exists()) {
                            path = String.format(
                                    "/" + getActivity().getString(R.string.app_name) + "/%d.jpg", System.currentTimeMillis());
                            outStream = new FileOutputStream(path);
                            outStream.write(data);
                            outStream.close();
                        } else {
                            if (file.mkdir()) {
                                path = String.format(
                                        "/" + getActivity().getString(R.string.app_name) + "/%d.jpg", System.currentTimeMillis());
                                 outStream = new FileOutputStream(path);
                                outStream.write(data);
                                outStream.close();
                            } else {
                                Toast.makeText(getActivity(), getActivity().getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Bundle b = new Bundle();
                    b.putString("path", path);

                    Intent intent = new Intent(getActivity(), CameraImagePriview.class);
                    intent.putExtra("path", path);
                    intent.putExtra("status", "camera");
                    intent.putExtra("ActivitStatus",isRoyality);
                    startActivity(intent);
                }
            }
        };

        gallery_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


                /*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_IMAGE);*/
            }
        });

        camera_changeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previewing) {
                    camera.stopPreview();
                }
                camera.release();

                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                camera = Camera.open(currentCameraId);
//            Camera.Parameters parameters = camera.getParameters();
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            camera.setParameters(parameters);
                camera.setDisplayOrientation(90);

                try {
                    camera.setPreviewDisplay(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }
        });

        camera_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flash) {
                    //ToDo something
//                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
//                flash
                } else {

                    //ToDo something
//                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    camera.release();

                }
            }
        });


        camera_capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, jpegCallback);


//                if (previewing) {
//                    camera.stopPreview();
//                }
//                camera.release();
//
////            if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
////                currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
////            } else {
////                currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
////            }
//                camera = Camera.open(currentCameraId);
////            Camera.Parameters parameters = camera.getParameters();
////            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
////            camera.setParameters(parameters);
//                camera.setDisplayOrientation(90);
//
//                try {
//                    camera.setPreviewDisplay(surfaceHolder);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                camera.startPreview();

            }
        });


    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        camera = Camera.open(0);
        camera.setDisplayOrientation(90);

        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        camera.setParameters(parameters);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                camera.setDisplayOrientation(90);
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}