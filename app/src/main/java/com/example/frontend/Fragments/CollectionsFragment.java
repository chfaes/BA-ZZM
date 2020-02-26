package com.example.frontend.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.frontend.Fragments.Dialogs.WebsiteDialog;
import com.example.frontend.Models.PatientDocument;
import com.example.frontend.Models.PatientImage;
import com.example.frontend.Models.PatientWebsite;
import com.example.frontend.Models.WebsiteType;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;
import com.example.frontend.Service.JsonPlaceHolderApi;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.frontend.Fragments.CollectionsFragment.Media.DOCUMENTS;
import static com.example.frontend.Fragments.CollectionsFragment.Media.GALLERY;
import static com.example.frontend.Fragments.CollectionsFragment.Media.VIDEOS;
import static com.example.frontend.Fragments.CollectionsFragment.Media.WEBSITES;

public class CollectionsFragment extends Fragment implements WebsiteDialog.WebsiteDialogListener {

    private int patientId;
    DatabaseHelper db;
    private RadioGroup rgMedia;
    private RadioButton rbGallery;
    private RadioButton rbVideos;
    private RadioButton rbDocuments;
    private RadioButton rbWebsites;
    private static View cview;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private int columnCounter = 1;
    private int longClickedMedia;
    Dialog myDialog;
    private int lastWebsitTypeId;
    private List<String> allImagePaths = new ArrayList<>();
    private List<String> allDocumentPaths = new ArrayList<>();
    private List<Integer> selectedWebsites;

    enum Media {
        GALLERY,
        VIDEOS,
        DOCUMENTS,
        WEBSITES
    }

    Media selectedMedia = GALLERY;
    ColorStateList defaultTextColor;
    ImageView btnAddWebsite;

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
        return inflater.inflate(R.layout.fragment_collections, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        cview = view;
        db = new DatabaseHelper(getContext());
        btnAddWebsite = view.findViewById(R.id.btnAddWebsite);
        btnAddWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebsiteDialog();
            }
        });
        rgMedia = view.findViewById(R.id.rgMedia);
        rbGallery = view.findViewById(R.id.rbGallery);
        rbVideos = view.findViewById(R.id.rbVideos);
        rbDocuments = view.findViewById(R.id.rbDocuments);
        rbWebsites = view.findViewById(R.id.rbWebsites);
        myDialog = new Dialog(getActivity());
        ll1 = (LinearLayout) view.findViewById(R.id.llFirstColumn);
        ll2 = (LinearLayout) view.findViewById(R.id.llSecondColumn);
        ll3 = (LinearLayout) view.findViewById(R.id.llThirdColumn);

        rgMedia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                setDrawablesBlack();
                clearScrollView();
                switch (checkedId) {
                    case R.id.rbGallery:
                        selectedMedia = GALLERY;
                        selectAllSelectedImages();
                        break;
                    case R.id.rbVideos:
                        selectedMedia = VIDEOS;
                        setUpVideos();
                        break;
                    case R.id.rbDocuments:
                        selectedMedia = DOCUMENTS;
                        selectAllSelectedDocuments();
                        break;
                    case R.id.rbWebsites:
                        selectedMedia = WEBSITES;
                        btnAddWebsite.setVisibility(View.VISIBLE);
                        selectAllSelectedWebsites();
                        break;
                }
            }
        });
        rbGallery.setChecked(true);
    }

    private void setDrawablesBlack() {
        rbGallery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_image_black, 0, 0, 0);
        rbVideos.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_movie_black, 0, 0, 0);
        rbDocuments.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_document_black, 0, 0, 0);
        rbWebsites.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_web_black, 0, 0, 0);
    }

    private void clearScrollView() {
        btnAddWebsite.setVisibility(View.INVISIBLE);
        columnCounter = 1;
        ll1.removeAllViews();
        ll2.removeAllViews();
        ll3.removeAllViews();
    }

    public ArrayList<String> getImagesPath() {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = getActivity().getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }

    public ArrayList<String> getVideosPath() {
        Uri uri;
        ArrayList<String> listOfAllVideos = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfVideo = null;
        uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME};

        cursor = getActivity().getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfVideo = cursor.getString(column_index_data);

            listOfAllVideos.add(PathOfVideo);
        }
        return listOfAllVideos;
    }

    public void getPDFsPath(File dir, List<String> selectedDocuments) {
        String pdfPattern = ".pdf";
        final File FileList[] = dir.listFiles();

        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {
                if (FileList[i].isDirectory()) {
                    getPDFsPath(FileList[i], selectedDocuments);
                } else {
                    if (FileList[i].getName().endsWith(pdfPattern)) {
                        allDocumentPaths.add(FileList[i].getPath());
                        final File file = FileList[i];
                        final LinearLayout llPdf = new LinearLayout(getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                350);
                        llPdf.setLayoutParams(lp);
                        final TextView newIv = new TextView(getContext());
                        lp.setMargins(0, 20, 0, 20);
                        newIv.setLayoutParams(lp);
                        newIv.setPadding(0, 25, 0, 15);
                        llPdf.setId(allDocumentPaths.indexOf(FileList[i].getPath()));
                        newIv.setText(FileList[i].getName());
                        newIv.setBackgroundColor(getResources().getColor(R.color.white));
                        defaultTextColor = newIv.getTextColors();
                        newIv.setGravity(Gravity.CENTER);
                        Drawable resizedDrawable = resize(getResources().getDrawable(R.drawable.pdf_thumbnail));

                        newIv.setCompoundDrawablesWithIntrinsicBounds(null, resizedDrawable, null, null);
                        newIv.setCompoundDrawablePadding(-15);
                        llPdf.addView(newIv);
                        llPdf.setPadding(15, 0, 15, 15);
                        llPdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showPDFPopup(file);
                            }
                        });
                        if(selectedDocuments.contains(FileList[i].getPath())){
                            llPdf.setSelected(true);
                            llPdf.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                        }
                        registerForContextMenu(llPdf);
                        switch (columnCounter) {
                            case 1:
                                ll1.addView(llPdf);
                                columnCounter++;
                                break;
                            case 2:
                                ll2.addView(llPdf);
                                columnCounter++;
                                break;
                            case 3:
                                ll3.addView(llPdf);
                                columnCounter = 1;
                                break;
                        }
                    }
                }
            }
        }
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 100, 100, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.media_menu, menu);
        longClickedMedia = v.getId();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        View selectedIv = getView().findViewById(longClickedMedia);
        switch (item.getItemId()) {
            case R.id.selectOption:
                if (selectedIv.isSelected()) {
                    selectedIv.setSelected(false);
                    selectedIv.setBackgroundColor(0);
                    switch (selectedMedia) {
                        case GALLERY:
                            PatientImage patientImage = new PatientImage();
                            patientImage.setImagePath(allImagePaths.get(longClickedMedia));
                            patientImage.setPatientId(patientId);
                            deletePatientImage(patientImage);
                            break;
                        case VIDEOS:
                            break;
                        case DOCUMENTS:
                            PatientDocument patientDocument = new PatientDocument();
                            patientDocument.setDocumentPath(allDocumentPaths.get(longClickedMedia));
                            patientDocument.setPatientId(patientId);
                            deletePatientDocument(patientDocument);
                            break;
                        case WEBSITES:
                            PatientWebsite patientWebsite = new PatientWebsite();
                            patientWebsite.setWebsiteTypeId(longClickedMedia);
                            patientWebsite.setPatientId(patientId);
                            deletePatientWebsite(patientWebsite);
                            break;
                    }
                } else {
                    selectedIv.setSelected(true);
                    selectedIv.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    switch (selectedMedia) {
                        case GALLERY:
                            PatientImage newPatientImage = new PatientImage();
                            newPatientImage.setImagePath(allImagePaths.get(longClickedMedia));
                            newPatientImage.setPatientId(patientId);
                            addNewPatientImage(newPatientImage);
                            break;
                        case VIDEOS:
                            break;
                        case DOCUMENTS:
                            PatientDocument newPatientDocument = new PatientDocument();
                            newPatientDocument.setDocumentPath(allDocumentPaths.get(longClickedMedia));
                            newPatientDocument.setPatientId(patientId);
                            addNewPatientDocument(newPatientDocument);
                            break;
                        case WEBSITES:
                            PatientWebsite newPatientWebsite = new PatientWebsite();
                            newPatientWebsite.setWebsiteTypeId(longClickedMedia);
                            newPatientWebsite.setPatientId(patientId);
                            addNewPatientWebsite(newPatientWebsite);
                            break;
                    }
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showImagePopup(Bitmap image) {
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

    private void showVideoPopup(String path) {
        myDialog.setContentView(R.layout.popup_video);
        TextView btnClose;
        final VideoView videoView = (VideoView) myDialog.findViewById(R.id.vvDisplay);
        videoView.setVideoPath(path);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        /*
                         * add media controller
                         */
                        MediaController mc = new MediaController(getContext());
                        videoView.setMediaController(mc);
                        /*
                         * and set its position on screen
                         */
                        mc.setAnchorView(videoView);

                        ((ViewGroup) mc.getParent()).removeView(mc);

                        ((FrameLayout) myDialog.findViewById(R.id.vvWrapper))
                                .addView(mc);
                        mc.setVisibility(View.VISIBLE);
                    }
                });
                videoView.start();

            }
        });


        btnClose = (TextView) myDialog.findViewById(R.id.btnCloseImage);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    private void showPDFPopup(File file) {
        myDialog.setContentView(R.layout.popup_pdf);
        TextView btnClose;
        PDFView pdfView = myDialog.findViewById(R.id.pdfView);
        pdfView.setBackgroundColor(getResources().getColor(R.color.black));
        pdfView.fromFile(file)
                .enableSwipe(true)
                .spacing(20)
                .load();
        btnClose = (TextView) myDialog.findViewById(R.id.btnCloseImage);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    private void showWebsitePopup(String url) {
        myDialog.setContentView(R.layout.popup_website);
        TextView btnClose;

        if (!url.startsWith("www.") && !url.startsWith("http://") && !url.startsWith("https://")) {
            url = "www." + url;
        }
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "https://" + url;
        }

        WebView webView = (WebView) myDialog.findViewById(R.id.websiteView);
        webView.loadUrl(url);

        btnClose = (TextView) myDialog.findViewById(R.id.btnCloseImage);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    private void setUpGallery(List<String> selectedImages) {
        rbGallery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_image_white, 0, 0, 0);
        allImagePaths = getImagesPath();
        for (final String path : allImagePaths) {
            ImageView newIv = new ImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    510);
            lp.setMargins(0, 20, 0, 20);
            newIv.setLayoutParams(lp);
            final Bitmap myBitmap = BitmapFactory.decodeFile(path);
            Bitmap cutBitmap = scaleCenterCrop(myBitmap, 220, 320);
            newIv.setAdjustViewBounds(true);
            newIv.setImageBitmap(cutBitmap);
            newIv.setPadding(15, 15, 15, 15);
            newIv.setId(allImagePaths.indexOf(path));
            newIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImagePopup(myBitmap);
                }
            });
            if (selectedImages.contains(path)) {
                newIv.setSelected(true);
                newIv.setBackgroundColor(getResources().getColor(R.color.colorBlue));
            }
            registerForContextMenu(newIv);
            switch (columnCounter) {
                case 1:
                    ll1.addView(newIv);
                    columnCounter++;
                    break;
                case 2:
                    ll2.addView(newIv);
                    columnCounter++;
                    break;
                case 3:
                    ll3.addView(newIv);
                    columnCounter = 1;
                    break;
            }
        }
    }

    private void setUpVideos() {
        rbVideos.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_movie_white, 0, 0, 0);
        List<String> allVideoPaths = new ArrayList<>();
        allVideoPaths = getVideosPath();
        for (final String path : allVideoPaths) {
            ImageView newIv = new ImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    510);
            lp.setMargins(0, 20, 0, 20);
            newIv.setLayoutParams(lp);
            final Bitmap myBitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            Bitmap cutBitmap = scaleCenterCrop(myBitmap, 220, 280);
            newIv.setAdjustViewBounds(true);
            newIv.setImageBitmap(cutBitmap);
            newIv.setPadding(15, 15, 15, 15);
            newIv.setId(allVideoPaths.indexOf(path));
            newIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showVideoPopup(path);
                }
            });
            switch (columnCounter) {
                case 1:
                    ll1.addView(newIv);
                    columnCounter++;
                    break;
                case 2:
                    ll2.addView(newIv);
                    columnCounter++;
                    break;
                case 3:
                    ll3.addView(newIv);
                    columnCounter = 1;
                    break;
            }
        }

    }

    private void setUpDocuments(List<String> selectedDocuments) {
        rbDocuments.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_document_white, 0, 0, 0);
        File dir = Environment.getExternalStorageDirectory();
        getPDFsPath(dir, selectedDocuments);
    }

    private void setUpWebsites(List<Integer> selectedWebsites) {
        rbWebsites.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_web_white, 0, 0, 0);
        addWebsiteButtons(selectedWebsites);
    }

    private void addWebsiteButtons(List<Integer> selectedWebsitesOfPatient) {
        selectedWebsites = selectedWebsitesOfPatient;
        //Get all WebsiteTypes of Patient
        List<WebsiteType> websiteTypes = db.getAllWebsiteTypes();
        for (final WebsiteType wt : websiteTypes) {
            addWebsiteButton(wt.getId(), wt);
        }

        /*Only used for Heruoku Database

        Call<List<WebsiteType>> call = jsonPlaceHolderApi.getAllWebsiteTypes();
        call.enqueue(new Callback<List<WebsiteType>>() {
            @Override
            public void onResponse(Call<List<WebsiteType>> call, Response<List<WebsiteType>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "GetPatientDiagnosesOfClass not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    List<WebsiteType> websiteTypes = response.body();
                    for (final WebsiteType wt : websiteTypes) {
                        addWebsiteButton(wt.getId(), wt);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<WebsiteType>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    private void addWebsiteButton(final int websiteId, final WebsiteType wt) {
        final LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                350);
        lp.setMargins(0,0,0,20);
        // TooltipCompat.setTooltipText(ll, wt.getDescription());
        ll.setPadding(15, 15, 15, 15);
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tvTitle = new TextView(getContext());
        LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                120);
        tvTitle.setLayoutParams(lpTitle);
        tvTitle.setText(wt.getName());
        tvTitle.setTextSize(22);
        tvTitle.setPadding(20, 20, 20, 0);
        tvTitle.setTextColor(getResources().getColor(R.color.colorDarkBlue));
        tvTitle.setBackgroundColor(getResources().getColor(R.color.white));

        TextView tvUrl = new TextView(getContext());
        LinearLayout.LayoutParams lpUrl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                220);
        tvUrl.setLayoutParams(lpUrl);
        tvUrl.setText(wt.getUrl());
        tvTitle.setTextSize(18);
        tvUrl.setPadding(20, 0, 20, 0);
        tvUrl.setBackgroundColor(getResources().getColor(R.color.white));

        ll.addView(tvTitle);
        ll.addView(tvUrl);

        if(selectedWebsites.contains(websiteId)){
            ll.setSelected(true);
            ll.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        }
        ll.setId(websiteId);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWebsitePopup(wt.getUrl());
            }
        });
        registerForContextMenu(ll);
        switch (columnCounter) {
            case 1:
                ll1.addView(ll);
                columnCounter++;
                break;
            case 2:
                ll2.addView(ll);
                columnCounter++;
                break;
            case 3:
                ll3.addView(ll);
                columnCounter = 1;
                break;
        }
    }

    public void addNewPatientImage(final PatientImage patientImage) {
        db.addPatientImage(patientImage);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPatientImage(patientImage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "create PatientImage NOT successful", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void deletePatientImage(final PatientImage patientImage) {
        db.deletePatientImage(patientImage);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deletePatientImage(patientImage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "delete PatientImage NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void selectAllSelectedImages() {
        setUpGallery(db.getAllImagePathsOfPatient(patientId));

         /*Only used for Heruoku Database

        Call<List<String>> call = jsonPlaceHolderApi.getAllImagePathsOfPatient(patientId);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    setUpGallery(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
            }
        }); */
    }

    public void addNewPatientDocument(final PatientDocument patientDocument) {
         db.addPatientDocument(patientDocument);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPatientDocument(patientDocument);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "create PatientDocument NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void deletePatientDocument(final PatientDocument patientDocument) {
        db.deletePatientDocument(patientDocument);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deletePatientDocument(patientDocument);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "delete PatientDocument NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void selectAllSelectedDocuments() {
        setUpDocuments(db.getAllDocumentPathsOfPatient(patientId));

        /*Only used for Heruoku Database

        Call<List<String>> call = jsonPlaceHolderApi.getAllDocumentPathsOfPatient(patientId);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    setUpDocuments(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
            }
        }); */
    }

    public void addNewPatientWebsite(final PatientWebsite patientWebsite) {
         db.addPatientWebsite(patientWebsite);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createPatientWebsite(patientWebsite);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "create PatientWebsite NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void deletePatientWebsite(final PatientWebsite patientWebsite) {
        db.deletePatientWebsite(patientWebsite);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.deletePatientWebsite(patientWebsite);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "delete PatientWebsite NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void selectAllSelectedWebsites() {
        setUpWebsites(db.getAllWebsiteIdsOfPatient(patientId));

         /*Only used for Heruoku Database

        Call<List<Integer>> call = jsonPlaceHolderApi.getAllWebsiteIdsOfPatient(patientId);
        call.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    setUpWebsites(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
            }
        }); */
    }


    public void addNewWebsiteType(final WebsiteType websiteType) {
        db.addWebsiteType(websiteType);
        getInsertWebsiteTypeId(websiteType);

         /*Only used for Heruoku Database

        Call<ResponseBody> call = jsonPlaceHolderApi.createWebsiteType(websiteType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getInsertWebsiteTypeId(websiteType);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "create WebsiteType NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void getInsertWebsiteTypeId(final WebsiteType websiteType) {
        lastWebsitTypeId = db.selectLastWebsiteId();
        addWebsiteButton(lastWebsitTypeId, websiteType);

         /*Only used for Heruoku Database

        Call<Integer> call = jsonPlaceHolderApi.getLastWebsiteTypeId();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lastWebsitTypeId = response.body();
                addWebsiteButton(lastWebsitTypeId, websiteType);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "get lastWebsiteTypeId NOT successful", Toast.LENGTH_SHORT).show();
            }
        }); */
    }


    public void openWebsiteDialog() {
        WebsiteDialog websiteDialog = new WebsiteDialog();
        websiteDialog.setTargetFragment(CollectionsFragment.this, 1);
        websiteDialog.show(getActivity().getSupportFragmentManager(), "Website Dialog");
    }

    @Override
    public void applyTexts(WebsiteType websiteType) {
        addNewWebsiteType(websiteType);
    }
}
