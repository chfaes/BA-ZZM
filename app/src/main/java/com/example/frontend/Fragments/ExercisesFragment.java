package com.example.frontend.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.Models.ExercisePhoto;
import com.example.frontend.Models.Note;
import com.example.frontend.Models.PatientExercise;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;
import com.example.frontend.Service.JsonPlaceHolderApi;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExercisesFragment extends Fragment {

    private int patientId;
    DatabaseHelper db;
    private List<PatientExercise> patientExercisesOfPatient = new ArrayList<>();
    private List<ExercisePhoto> allExercisesPhotosOfPatient = new ArrayList<>();

    private ImageView ivObservation;
    private ImageView ivElongation;
    private ImageView ivPatch;
    private ImageView ivTemple;
    private ImageView ivCheek;
    private ImageButton btnCamera;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private LinearLayout llPictures;
    private int lastPhotoId;

    Dialog myDialog;


    private View.OnClickListener onClickListener;

    /*Only used for Heruoku Database

    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://consapp.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        patientId = getArguments().getInt("patientId");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercises, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());

        myDialog = new Dialog(getActivity());
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exerciseTitle = "";
                switch (view.getId()) {
                    case R.id.iv_observation:
                        exerciseTitle = "Selbstbeobachtung";
                        break;
                    case R.id.iv_elongation:
                        exerciseTitle = "Muskeldehnung";
                        break;
                    case R.id.iv_patch:
                        exerciseTitle = "Medikamentenpflaster";
                        break;
                    case R.id.iv_cheek:
                        exerciseTitle = "MassageWange";
                        break;
                    case R.id.iv_temple:
                        exerciseTitle = "MassageSchlaefe";
                        break;
                }
                if (view.isSelected()) {
                    view.setSelected(false);
                    view.setBackgroundColor(0);

                    removePatientExercise(patientId, exerciseTitle);
                } else {
                    view.setSelected(true);
                    view.setBackgroundColor(getResources().getColor(R.color.colorBlue));

                    PatientExercise newPatientExercise = new PatientExercise();
                    newPatientExercise.setExerciseTypeTitle(exerciseTitle);
                    newPatientExercise.setPatientId(patientId);
                    addPatientExercise(newPatientExercise);
                }
            }
        };
        ivObservation = (ImageView) view.findViewById(R.id.iv_observation);
        ivObservation.setOnClickListener(onClickListener);
        ivElongation = (ImageView) view.findViewById(R.id.iv_elongation);
        ivElongation.setOnClickListener(onClickListener);
        ivPatch = (ImageView) view.findViewById(R.id.iv_patch);
        ivPatch.setOnClickListener(onClickListener);
        ivTemple = (ImageView) view.findViewById(R.id.iv_temple);
        ivTemple.setOnClickListener(onClickListener);
        ivCheek = (ImageView) view.findViewById(R.id.iv_cheek);
        ivCheek.setOnClickListener(onClickListener);

        llPictures = (LinearLayout) view.findViewById(R.id.llPictures);
        btnCamera = (ImageButton) view.findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (imageTakeIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        addExercisePhotosToView();
        showInitialSelection();
    }

    public void addExercisePhotosToView() {
        allExercisesPhotosOfPatient = db.getAllExercisePhotosOfPatient(patientId);
        for (ExercisePhoto photo : allExercisesPhotosOfPatient) {
            byte[] photoBytes = photo.getPhotoBytes();
            final Bitmap bmp = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
            addBitmapToView(bmp, photo.getId());
        }

        /*Only used for Heruoku Database

        Call<List<ExercisePhoto>> call = jsonPlaceHolderApi.getExercisePhotosOfPatient(patientId);
        call.enqueue(new Callback<List<ExercisePhoto>>() {
            @Override
            public void onResponse(Call<List<ExercisePhoto>> call, Response<List<ExercisePhoto>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "not succesful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allExercisesPhotosOfPatient = response.body();
                    for (ExercisePhoto photo : allExercisesPhotosOfPatient) {
                        byte[] photoBytes = photo.getPhotoBytes();
                        final Bitmap bmp = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
                        addBitmapToView(bmp, photo.getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ExercisePhoto>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        }); */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        addNewExercisePhoto(imageBitmap);
    }

    public byte[] bitmapToByte(Bitmap drawing) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        drawing.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] drawingByteArray = bos.toByteArray();
        return drawingByteArray;
    }

    public void showInitialSelection() {
        patientExercisesOfPatient = db.getAllExercisesOfPatient(patientId);
        for (PatientExercise patientExercise : patientExercisesOfPatient) {
            switch (patientExercise.getExerciseTypeTitle()) {
                case "Selbstbeobachtung":
                    ivObservation.setSelected(true);
                    ivObservation.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    break;
                case "Muskeldehnung":
                    ivElongation.setSelected(true);
                    ivElongation.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    break;
                case "Medikamentenpflaster":
                    ivPatch.setSelected(true);
                    ivPatch.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    break;
                case "MassageWange":
                    ivCheek.setSelected(true);
                    ivCheek.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    break;
                case "MassageSchlaefe":
                    ivTemple.setSelected(true);
                    ivTemple.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    break;
            }
        }

        /*Only used for Heruoku Database

        Call<List<PatientExercise>> call = jsonPlaceHolderApi.getSelectedPatientExercises(patientId);
        call.enqueue(new Callback<List<PatientExercise>>() {
            @Override
            public void onResponse(Call<List<PatientExercise>> call, Response<List<PatientExercise>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "not succesful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    patientExercisesOfPatient = response.body();
                    for (PatientExercise patientExercise : patientExercisesOfPatient) {
                        switch (patientExercise.getExerciseTypeTitle()) {
                            case "Selbstbeobachtung":
                                ivObservation.setSelected(true);
                                ivObservation.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                break;
                            case "Muskeldehnung":
                                ivElongation.setSelected(true);
                                ivElongation.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                break;
                            case "Medikamentenpflaster":
                                ivPatch.setSelected(true);
                                ivPatch.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                break;
                            case "MassageWange":
                                ivCheek.setSelected(true);
                                ivCheek.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                break;
                            case "MassageSchlaefe":
                                ivTemple.setSelected(true);
                                ivTemple.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PatientExercise>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        }); */
    }

    public void addPatientExercise(PatientExercise patientExercise) {
        db.addPatientExercise(patientExercise);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPatientExercise(patientExercise);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createPatientExercise NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void removePatientExercise(int patientId, String exerciseTitle) {
        db.deletePatientExercise(patientId, exerciseTitle);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deletePatientExercise(patientId, exerciseTitle);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        }); */
    }

    public void addBitmapToView(final Bitmap photo, final int exercisePhotoId) {
        final ImageView image = new ImageView(getContext());
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(0, 0, 0, 20);
        image.setId(exercisePhotoId);
        image.setLayoutParams(param);
        image.setAdjustViewBounds(true);
        image.setPadding(5, 5, 5, 5);
        image.setImageBitmap(photo);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(photo);
            }
        });
        registerForContextMenu(image);
        llPictures.addView(image);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.exercisephoto_menu, menu);
        lastPhotoId = v.getId();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteOption:
                deleteExercisePhoto(lastPhotoId);
                llPictures.removeView(getView().findViewById(lastPhotoId));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void addNewExercisePhoto(final Bitmap photoBitmap) {
        ExercisePhoto newPhoto = new ExercisePhoto();
        newPhoto.setPatientId(patientId);
        newPhoto.setPhotoBytes(bitmapToByte(photoBitmap));

        db.addExercisePhoto(newPhoto);
        getInsertPhotoId(photoBitmap);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createExercisePhoto(newPhoto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getInsertPhotoId(photoBitmap);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createNote NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void getInsertPhotoId(final Bitmap photoBitmap) {
        lastPhotoId = db.selectLastPhotoId();
        addBitmapToView(photoBitmap, lastPhotoId);

        /*Only used for Heruoku Database

        Call<Integer> call = jsonPlaceHolderApi.getLastPhotoId();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lastPhotoId = response.body();
                addBitmapToView(photoBitmap, lastPhotoId);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), "getId NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void deleteExercisePhoto(int photoId) {
        db.deleteExercisePhoto(photoId);

        /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deleteExercisePhoto(photoId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        }); */
    }

    private void showPopup(Bitmap image) {
        myDialog.setContentView(R.layout.popup_image);
        TextView btnClose;
        PhotoView photoView = (PhotoView) myDialog.findViewById(R.id.ivDisplay);
        photoView.setImageBitmap(image);
        btnClose = (TextView) myDialog.findViewById(R.id.btnCloseImage);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
}
