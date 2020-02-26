package com.example.frontend.Fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.frontend.Fragments.Notes.PaintView;
import com.example.frontend.Globals;
import com.example.frontend.Models.Note;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;
import com.example.frontend.Service.JsonPlaceHolderApi;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment {

    private PaintView paintView;
    DatabaseHelper db;
    private View cView;
    private int patientId;
    private LinearLayout linearLayout;
    private ImageView chosenImageView;
    private int paintWidth = Globals.getInstance().getFragmentWidth() / 4 * 3;
    private int paintHeight = Globals.getInstance().getFragmentHeight();
    Bitmap bmp = Bitmap.createBitmap(paintWidth, paintHeight, Bitmap.Config.ARGB_8888);
    Bitmap alteredBitmap = Bitmap.createBitmap(paintWidth, paintHeight, Bitmap.Config.ARGB_8888);
    Canvas canvas;
    Paint paint;
    Matrix matrix;
    float downx = 0;
    float downy = 0;
    float upx = 0;
    float upy = 0;
    boolean init = true;
    boolean eraserMode = false;
    private MenuItem eraserItem;
    List<Note> allNotesOfPatient = new ArrayList<>();
    int lastNoteId;
    Context context;

     /*Only used for Heruoku Database

    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://consapp.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class); */


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        patientId = getArguments().getInt("patientId");
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.post(new Runnable() {
                    public void run() {

                    }
                });
            }
        });
        cView = view;
        context = view.getContext();
        linearLayout = (LinearLayout) cView.findViewById(R.id.llPictures);
        chosenImageView = (ImageView) view.findViewById(R.id.ChoosenImageView);
        setUpCanvas();
        db = new DatabaseHelper(getContext());
        addPatientNotesToView();
    }

    public void addPatientNotesToView() {
        allNotesOfPatient = db.getAllNotesOfPatient(patientId);
        for (Note note : allNotesOfPatient) {
            if (note != null) {
                addByteArrayToView(note.getId(), note.getNoteBytes(), note.isSelected());
            }
        }
        linearLayout.invalidate();

         /*Only used for Heruoku Database

        Call<List<Note>> call = jsonPlaceHolderApi.getAllNotesOfPatient(patientId);
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "not succesful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allNotesOfPatient = response.body();
                    for (Note note : allNotesOfPatient) {
                        if (note != null) {
                            addByteArrayToView(note.getId(), note.getNoteBytes(), note.isSelected());
                        }
                    }
                    linearLayout.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        }); */
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setUpCanvas() {
        if (init) {
            canvas = new Canvas(alteredBitmap);
            canvas.drawColor(0xffffffff);
            paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(6);
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setXfermode(null);
            paint.setAlpha(0xff);
            init = false;
        }
        chosenImageView.setImageBitmap(alteredBitmap);
        chosenImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        downx = event.getX();
                        downy = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        upx = event.getX();
                        upy = event.getY();
                        canvas.drawLine(downx, downy, upx, upy, paint);
                        chosenImageView.invalidate();
                        downx = upx;
                        downy = upy;
                        break;
                    case MotionEvent.ACTION_UP:
                        upx = event.getX();
                        upy = event.getY();
                        canvas.drawLine(downx, downy, upx, upy, paint);
                        chosenImageView.invalidate();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pen_menu, menu);
        eraserItem = menu.findItem(R.id.rubber);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rubber:
                if (!eraserMode) {
                    item.setIcon(R.drawable.ic_pen_white);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    paint.setStrokeWidth(15);
                    eraserMode = true;
                } else {
                    item.setIcon(R.drawable.ic_rubber);
                    paint.setXfermode(null);
                    paint.setStrokeWidth(3);
                    eraserMode = false;
                }
                return true;
            case R.id.template_save:
                saveAsTemplate(alteredBitmap);
                return true;
            case R.id.template_open:
                openTemplate();
                return true;
            case R.id.clear:
                init = true;
                setUpCanvas();
                return true;
            case R.id.save:
                byte[] savedByte = bitmapToByte(alteredBitmap);
                Note newNote = new Note();
                newNote.setSelected(false);
                newNote.setPatientId(patientId);
                newNote.setNoteBytes(savedByte);
                addNewNote(newNote);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public byte[] bitmapToByte(Bitmap drawing) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        drawing.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] drawingByteArray = bos.toByteArray();
        return drawingByteArray;
    }

    public void addByteArrayToView(final int noteId, final byte[] drawing, boolean isSelected) {
        final Bitmap bmp = BitmapFactory.decodeByteArray(drawing, 0, drawing.length);
        final ImageView image = new ImageView(context);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(0, 0, 0, 20);
        image.setId(noteId);
        image.setLayoutParams(param);
        image.setAdjustViewBounds(true);
        image.setPadding(15, 15, 15, 15);
        if (isSelected) {
            image.setSelected(true);
            image.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        }

        image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                if (eraserMode) {
                    eraserItem.setIcon(R.drawable.ic_rubber);
                    paint.setXfermode(null);
                    paint.setStrokeWidth(3);
                    eraserMode = false;
                }
                alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
                        .getHeight(), bmp.getConfig());
                canvas = new Canvas(alteredBitmap);
                Matrix matrix = new Matrix();
                canvas.drawBitmap(bmp, matrix, paint);

                chosenImageView.setImageBitmap(alteredBitmap);
                chosenImageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                downx = event.getX();
                                downy = event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                upx = event.getX();
                                upy = event.getY();
                                canvas.drawLine(downx, downy, upx, upy, paint);
                                chosenImageView.invalidate();
                                downx = upx;
                                downy = upy;
                                break;
                            case MotionEvent.ACTION_UP:
                                upx = event.getX();
                                upy = event.getY();
                                canvas.drawLine(downx, downy, upx, upy, paint);
                                chosenImageView.invalidate();
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        image.setImageBitmap(bmp);
        registerForContextMenu(image);
        linearLayout.addView(image);
    }

    public void addNewNote(final Note note) {
        db.addNote(note);
        getInsertNoteId(note.getNoteBytes());

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createNote(note);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getInsertNoteId(note.getNoteBytes());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createNote NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void updateNote(int noteId, final Note updatedNote) {
        db.updateNote(noteId,updatedNote);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.updateNote(noteId, updatedNote);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "createNote NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void saveAsTemplate(Bitmap bitmap) {
        if (eraserMode) {
            eraserItem.setIcon(R.drawable.ic_rubber);
            paint.setXfermode(null);
            paint.setStrokeWidth(3);
            eraserMode = false;
        }
        if (bitmap != null) {
            ContentValues contentValues = new ContentValues(3);
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Draw On Me");

            Uri imageFileUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                OutputStream imageFileOS = getActivity().getContentResolver().openOutputStream(imageFileUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS);
                Toast t = Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_SHORT);
                t.show();

            } catch (Exception e) {
                Log.v("EXCEPTION", e.getMessage());
            }
        }
    }

    public void openTemplate() {
        if (eraserMode) {
            eraserItem.setIcon(R.drawable.ic_rubber);
            paint.setXfermode(null);
            paint.setStrokeWidth(3);
            eraserMode = false;
        }
        Intent choosePictureIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(choosePictureIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            Uri imageFileUri = intent.getData();
            try {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(
                        imageFileUri), null, bmpFactoryOptions);

                alteredBitmap = Bitmap.createBitmap(paintWidth, paintHeight, bmp.getConfig());
                canvas = new Canvas(alteredBitmap);
                matrix = new Matrix();

                int width = 0;
                int height = 0;
                if (paintHeight < bmp.getHeight() || paintWidth < bmp.getWidth()) {
                    bmp = resize(bmp, paintWidth, paintHeight);
                }
                if (paintHeight > bmp.getHeight()) {
                    height = paintHeight / 2 - bmp.getHeight() / 2;
                }
                if (paintWidth > bmp.getWidth()) {
                    width = paintWidth / 2 - bmp.getWidth() / 2;
                }

                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bmp, width, height, paint);

                chosenImageView.setImageBitmap(alteredBitmap);
                chosenImageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                downx = event.getX();
                                downy = event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                upx = event.getX();
                                upy = event.getY();
                                canvas.drawLine(downx, downy, upx, upy, paint);
                                chosenImageView.invalidate();
                                downx = upx;
                                downy = upy;
                                break;
                            case MotionEvent.ACTION_UP:
                                upx = event.getX();
                                upy = event.getY();
                                canvas.drawLine(downx, downy, upx, upy, paint);
                                chosenImageView.invalidate();
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            } catch (Exception e) {
                Log.v("ERROR", e.toString());
            }
        }
    }

    public void getInsertNoteId(final byte[] noteByte) {
        lastNoteId = db.selectLastNoteId();
        addByteArrayToView(lastNoteId, noteByte, false);

         /*Only used for Heruoku Database

        Call<Integer> call = jsonPlaceHolderApi.getLastNoteId();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lastNoteId = response.body();
                addByteArrayToView(lastNoteId, noteByte, false);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), "getId NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void deleteNote(int noteId) {
        db.deleteNote(noteId);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deleteNote(noteId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.note_menu, menu);
        lastNoteId = v.getId();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ImageView selectedIv = getView().findViewById(lastNoteId);
        switch (item.getItemId()) {
            case R.id.selectOption:
                if (selectedIv.isSelected()) {
                    selectedIv.setSelected(false);
                    selectedIv.setBackgroundColor(0);
                    Note updatedNote = new Note();
                    updatedNote.setSelected(false);
                    updatedNote.setPatientId(patientId);
                    updateNote(lastNoteId, updatedNote);
                } else {
                    selectedIv.setSelected(true);
                    selectedIv.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    Note updatedNote = new Note();
                    updatedNote.setSelected(true);
                    updatedNote.setPatientId(patientId);
                    updateNote(lastNoteId, updatedNote);
                }
                return true;
            case R.id.deleteOption:
                deleteNote(lastNoteId);
                linearLayout.removeView(selectedIv);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
