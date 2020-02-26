package com.example.frontend.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.Fragments.CollectionsFragment;
import com.example.frontend.Fragments.DiagnosisFragment;
import com.example.frontend.Fragments.DrugsFragment;
import com.example.frontend.Fragments.ExercisesFragment;
import com.example.frontend.Fragments.ModelsFragment;
import com.example.frontend.Fragments.NotesFragment;
import com.example.frontend.Fragments.OverviewFragment;
import com.example.frontend.Fragments.PainFragment;
import com.example.frontend.Fragments.PsychosocialFragment;
import com.example.frontend.Fragments.TranslatorFragment;
import com.example.frontend.Globals;
import com.example.frontend.Models.DiagnosisType;
import com.example.frontend.Models.DrugType;
import com.example.frontend.Models.ExercisePhoto;
import com.example.frontend.Models.ExerciseType;
import com.example.frontend.Models.Note;
import com.example.frontend.Models.Patient;
import com.example.frontend.Models.PatientDiagnosis;
import com.example.frontend.Models.PatientDocument;
import com.example.frontend.Models.PatientDrug;
import com.example.frontend.Models.PatientExercise;
import com.example.frontend.Models.PatientImage;
import com.example.frontend.Models.PatientWebsite;
import com.example.frontend.Models.WebsiteType;
import com.example.frontend.R;
import com.example.frontend.Service.DatabaseHelper;
import com.example.frontend.Service.JsonPlaceHolderApi;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;


import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.itextpdf.text.html.HtmlTags.FONT;

public class MenuActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1000;
    private static final int READ_STORAGE_CODE = 2000;
    DatabaseHelper db;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvUsername;
    private Patient patient;
    private Bundle bundlePatientId = new Bundle();
    private List<Note> allNotesOfPatient = new ArrayList<>();
    private List<DiagnosisType> allDiagnosisTypes = new ArrayList<>();
    private List<PatientDiagnosis> allPatientDiagnoses = new ArrayList<>();
    private List<DrugType> allDrugTypes = new ArrayList<>();
    private List<PatientDrug> allPatientDrugs = new ArrayList<>();
    private List<PatientExercise> allPatientExercises = new ArrayList<>();
    private List<ExercisePhoto> allExercisPhotos = new ArrayList<>();
    private List<ExerciseType> allExerciseTypes = new ArrayList<>();
    private List<String> allPatientImagesPath = new ArrayList<>();
    private List<String> allPatientDocumentsPath = new ArrayList<>();
    private List<Integer> allPatientWebsites = new ArrayList<>();
    private List<WebsiteType> allWebsiteTypes = new ArrayList<>();
    private Chunk newLineChunk = new Chunk("\n");

    /* Only used for Heroku Database
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://consapp.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_menu);

        setTitle(getString(R.string.overview));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patient = (Patient) extras.getSerializable("patient");
            Globals g = Globals.getInstance();
            g.setPatient(patient);
            bundlePatientId.putInt("patientId", patient.getId());
        }

        db = new DatabaseHelper(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        drawerLayout = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigationView);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OverviewFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_overview);
        }

        ViewTreeObserver vto = findViewById(R.id.fragment_container).getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                findViewById(R.id.fragment_container).getViewTreeObserver().removeOnPreDrawListener(this);
                Globals g = Globals.getInstance();
                g.setFragmentHeight(findViewById(R.id.fragment_container).getMeasuredHeight());
                g.setFragmentWidth(findViewById(R.id.fragment_container).getMeasuredWidth());
                return true;
            }
        });

        tvUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        tvUsername.setText(patient.getShortname());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_overview:
                        setTitle(getString(R.string.overview));
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OverviewFragment()).commit();
                        break;
                    case R.id.nav_models:
                        setTitle(getString(R.string.models));
                        ModelsFragment modelsFrag = new ModelsFragment();
                        modelsFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, modelsFrag).commit();
                        break;
                    case R.id.nav_pen:
                        setTitle(getString(R.string.notes));
                        NotesFragment notesFrag = new NotesFragment();
                        notesFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, notesFrag).commit();
                        break;
                    case R.id.nav_collections:
                        if (ContextCompat.checkSelfPermission(MenuActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Permission is not granted
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(MenuActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    READ_STORAGE_CODE);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.

                        } else {
                            setTitle(getString(R.string.collections));
                            CollectionsFragment collectionsFrag = new CollectionsFragment();
                            collectionsFrag.setArguments(bundlePatientId);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionsFrag).commit();
                            navigationView.setCheckedItem(R.id.nav_collections);
                        }
                        break;
                    case R.id.nav_psychosocial:
                        setTitle(getString(R.string.psychosocial));
                        PsychosocialFragment psychosocialFrag = new PsychosocialFragment();
                        psychosocialFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, psychosocialFrag).commit();
                        break;
                        /*
                    case R.id.nav_recognizer:
                        setTitle(getString(R.string.recognizer));
                        RecognizerFragment recognizerFrag = new RecognizerFragment();
                        recognizerFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recognizerFrag).commit();
                        break;
                        */
                    case R.id.nav_translator:
                        setTitle(getString(R.string.translator));
                        TranslatorFragment translatorFrag = new TranslatorFragment();
                        translatorFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, translatorFrag).commit();
                        break;
                    case R.id.nav_pain:
                        setTitle(getString(R.string.paindetails));
                        PainFragment painFrag = new PainFragment();
                        painFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, painFrag).commit();
                        break;
                    case R.id.nav_diagnosis:
                        setTitle(getString(R.string.diagnosis));
                        DiagnosisFragment diagnosisFrag = new DiagnosisFragment();
                        diagnosisFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, diagnosisFrag).commit();
                        break;
                    case R.id.nav_drugs:
                        setTitle(getString(R.string.drugs));
                        DrugsFragment drugsFrag = new DrugsFragment();
                        drugsFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, drugsFrag).commit();
                        break;
                    case R.id.nav_exercises:
                        setTitle(getString(R.string.exercises));
                        ExercisesFragment exercisesFrag = new ExercisesFragment();
                        exercisesFrag.setArguments(bundlePatientId);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exercisesFrag).commit();
                        break;
                    case R.id.nav_pdf:
                        /*
                        setTitle("Patienten");
                        Intent intent = new Intent(MenuActivity.this, PatientSelectionActivity.class);
                        startActivity(intent);*/
                        //handle runtime permission for devices with marshmallow and above
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            //system OS >= Marshmallow (6.0), check if permission is enabled or not
                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                //permission was not granted, request it
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions, STORAGE_CODE);
                            } else {
                                //permission already granted
                                createPdf();
                            }
                        } else {
                            //system OS < Marshmallow no required to check runtime permission
                        }
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void createPdf() {
        getAllDrugTypes();
        getPatientDrugs();
        getAllExerciseTypes();
        getPatientExercises();
        getPatientDocuments();
        getPatientImages();
        getAllWebsiteTypes();
        getPatientWebsites();
        getPatientDiagnoses();
    }

    private void savePdf() {
        Document mDoc = new Document();
        //pdf file name
        String fileName = patient.getShortname() + " - Zusammenfassung" ;
        //pdf file path
        final String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName + ".pdf";
        try {
            PdfWriter writer = PdfWriter.getInstance(mDoc, new FileOutputStream(filePath));
            //PdfWriter.getInstance(mDoc, new FileOutputStream(filePath)).setStrictImageSequence(true);
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
            BaseColor darkBlueColor = new BaseColor(15, 28, 75);
            Font fontTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Font fontParagraphTitle = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, darkBlueColor);
            Font fontListTitle = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, BaseColor.DARK_GRAY);
            //open the document for writing
            mDoc.setMargins(70, 70, 80, 80);
            mDoc.open();
            mDoc.addAuthor("Alice Truong");
            mDoc.addTitle("Schmerzsprechstunde vom " + timeStamp);
            mDoc.addCreationDate();

            String strGreeting = "";
            //add paragraph to the document
            if (patient.getGender().equals("Female")) {
                strGreeting = "Sehr geehrte Frau " + patient.getShortname() + ",";
            } else {
                strGreeting = "Sehr geehrter Herr " + patient.getShortname() + ",";
            }
            addLineBreak(mDoc, 8);
            Chunk title = new Chunk("Schmerzsprechstunde am Zentrum für Zahnmedizin", fontTitle);
            Paragraph pTitle = new Paragraph(title);
            mDoc.add(pTitle);

            addLineBreak(mDoc, 2);
            Paragraph pGreeting = new Paragraph(strGreeting);
            mDoc.add(pGreeting);
            addLineBreak(mDoc, 1);
            Paragraph pIntroduction = new Paragraph(new Paragraph("gerne berichten wir über Ihre Schmerzsprechstunde vom " + timeStamp + " und geben Ihnen eine Zusammenfassung Ihrer Befunde und des weiteren Vorgehens."));
            pIntroduction.setAlignment(Element.ALIGN_JUSTIFIED);
            mDoc.add(pIntroduction);

            Drawable d = getResources().getDrawable(R.drawable.uzh_logo);
            Image uzh_logo = createImageWithDrawable(d);
            uzh_logo.scalePercent(9);
            uzh_logo.setAbsolutePosition(380, 730);
            mDoc.add(uzh_logo);

            if (!allPatientDiagnoses.isEmpty()) {
                addLineBreak(mDoc, 1);
                Chunk diagnosisTitle = new Chunk("Diagnose", fontParagraphTitle);
                Paragraph pDiagnosisTitle = new Paragraph(diagnosisTitle);
                mDoc.add(pDiagnosisTitle);
                String strFollowingDiagnosis = "folgende Diagnose";
                if (allPatientDiagnoses.size() > 1) {
                    strFollowingDiagnosis = "folgenden Diagnosen";
                }
                Paragraph pDiagnosisIntroduction = new Paragraph("Aufgrund Ihrer Symptome und der bisherigen Befunde wurden die " + strFollowingDiagnosis + " gestellt:");
                mDoc.add(pDiagnosisIntroduction);
                com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
                for (PatientDiagnosis pd : allPatientDiagnoses) {
                    for (DiagnosisType dt : allDiagnosisTypes) {
                        if (pd.getDiagnosisId() == dt.getId()) {
                            ListItem listItem = new ListItem();
                            Chunk diagnosisName = new Chunk(dt.getName(), fontListTitle);
                            listItem.add(diagnosisName);
                            if (pd.getComment() != null) {
                                Chunk strDiagnosisComment = new Chunk(" (" + pd.getComment() + ")", fontListTitle);
                                listItem.add(strDiagnosisComment);
                            }
                            if (dt.getDescription() != null) {
                                Chunk strDiagnosisDesc = new Chunk("\n" + dt.getDescription());
                                listItem.add(strDiagnosisDesc);
                            }
                            listItem.setAlignment(Element.ALIGN_JUSTIFIED);
                            list.add(listItem);
                        }
                    }
                }
                mDoc.add(list);
            }
            if (!allPatientDrugs.isEmpty()) {
                addLineBreak(mDoc, 1);
                Chunk diagnosisTitle = new Chunk("Medikamente", fontParagraphTitle);
                Paragraph pDiagnosisTitle = new Paragraph(diagnosisTitle);
                mDoc.add(pDiagnosisTitle);
                String strFollowingDrugs = "";
                if (allPatientDrugs.size() > 1) {
                    strFollowingDrugs = "folgenden Medikamenten";
                }

                com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
                for (PatientDrug pd : allPatientDrugs) {
                    for (DrugType dt : allDrugTypes) {
                        if (pd.getDrugTypeId() == dt.getId()) {
                            ListItem listItem = new ListItem();
                            Chunk diagnosisName = new Chunk(dt.getName(), fontListTitle);
                            listItem.add(diagnosisName);
                            if (pd.getAmount() != null && !pd.getAmount().isEmpty()) {
                                Chunk strDiagnosisAmount = new Chunk(" (" + pd.getAmount() + ")", fontListTitle);
                                listItem.add(strDiagnosisAmount);
                            }
                            if (pd.getDosis() != null && !pd.getDosis().isEmpty()) {
                                Chunk strDiagnosisDosis = new Chunk(" (" + pd.getDosisInText() + ")", fontListTitle);
                                listItem.add(strDiagnosisDosis);
                            }
                            if (dt.getDescription() != null) {
                                Chunk strDiagnosisDesc = new Chunk("\n" + dt.getDescription());
                                listItem.add(strDiagnosisDesc);
                            }
                            listItem.setAlignment(Element.ALIGN_JUSTIFIED);
                            list.add(listItem);
                        }
                    }
                }
                mDoc.add(new Paragraph("Wir empfehlen Ihnen die Einnahme von" + strFollowingDrugs + ":"));
                mDoc.add(list);
            }
            if (!allPatientExercises.isEmpty()) {
                addLineBreak(mDoc, 1);
                Chunk exerciseTitle = new Chunk("Übungen", fontParagraphTitle);
                Paragraph pExerciseTitle = new Paragraph(exerciseTitle);
                mDoc.add(pExerciseTitle);
                mDoc.add(new Paragraph("Um Ihre Verspannungen im Gesicht zu lösen empfehlen wir Ihnen Folgendes:"));
                com.itextpdf.text.List listExercises = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
                for (PatientExercise pe : allPatientExercises) {
                    for (ExerciseType et : allExerciseTypes) {
                        if (pe.getExerciseTypeTitle().equals(et.getTitle())) {
                            Paragraph listItem = new ListItem();
                            Chunk exerciseName = new Chunk();
                            Drawable exerciseDrawable = null;
                            switch (et.getTitle()) {
                                case "Selbstbeobachtung":
                                    exerciseName = new Chunk("Entspannte Unterkieferhaltung und Selbstbeobachtung", fontListTitle);
                                    exerciseDrawable = getResources().getDrawable(R.drawable.uebung_selbstbeobachtung);
                                    break;
                                case "Muskeldehnung":
                                    exerciseName = new Chunk("Muskeldehnung", fontListTitle);
                                    exerciseDrawable = getResources().getDrawable(R.drawable.uebung_muskeldehnung);
                                    break;
                                case "Medikamentenpflaster":
                                    exerciseName = new Chunk("Medikamentenpflaster", fontListTitle);
                                    exerciseDrawable = getResources().getDrawable(R.drawable.uebung_pflaster);
                                    break;
                                case "MassageWange":
                                    exerciseName = new Chunk("Massage der Wangenmuskulatur", fontListTitle);
                                    exerciseDrawable = getResources().getDrawable(R.drawable.uebung_massagewangen);
                                    break;
                                case "MassageSchlaefe":
                                    exerciseName = new Chunk("Massage der Schläfenmuskulatur", fontListTitle);
                                    exerciseDrawable = getResources().getDrawable(R.drawable.uebung_massageschlaefe);
                                    break;
                            }
                            listItem.add(exerciseName);
                            if (et.getExplanation() != null) {
                                Chunk strDiagnosisDesc = new Chunk("\n" + et.getExplanation());
                                listItem.add(strDiagnosisDesc);
                            }
                            listItem.add(newLineChunk);
                            listItem.add(newLineChunk);
                            Image exerciseImage = createImageWithDrawable(exerciseDrawable);
                            exerciseImage.scalePercent(40);
                            Chunk imageChunk = new Chunk(exerciseImage, 0, 0, true);
                            listItem.add(imageChunk);
                            listItem.add(newLineChunk);
                            listItem.setAlignment(Element.ALIGN_JUSTIFIED);
                            listExercises.add(listItem);
                        }
                    }
                }
                mDoc.add(listExercises);
            }
            if (!allExercisPhotos.isEmpty()) {
                addLineBreak(mDoc, 1);
                mDoc.add(new Paragraph("Fotos aus der Sprechstunde als Unterstützung:\n", fontListTitle));
                Paragraph photoParagraph = new Paragraph();
                for (ExercisePhoto photo : allExercisPhotos) {
                    Image exerciseImage = Image.getInstance(photo.getPhotoBytes());
                    exerciseImage.setBorder(Rectangle.TOP);
                    exerciseImage.setBorderColor(BaseColor.WHITE);
                    exerciseImage.setBorderWidth(10f);
                    Chunk imageChunk = new Chunk(exerciseImage, 0, 0, true);
                    photoParagraph.add(imageChunk);
                    photoParagraph.add(new Chunk("      "));
                }
                mDoc.add(photoParagraph);
            }
            mDoc.newPage();
            if (!allNotesOfPatient.isEmpty()) {
                Chunk notesTitle = new Chunk("A1: Notizen/Zeichnungen", fontParagraphTitle);
                Paragraph pNotesTitle = new Paragraph(notesTitle);
                mDoc.add(pNotesTitle);
                if (allNotesOfPatient.size() > 1) {
                    mDoc.add(new Paragraph("Folgende Notizen/Zeichnungen wurden während der Sprechstunde für Sie erstellt:"));
                } else {
                    mDoc.add(new Paragraph("Die folgende Notiz/Zeichnung wurde während der Sprechstunde für Sie erstellt:"));
                }
                for (Note note : allNotesOfPatient) {
                    Paragraph pNote = new Paragraph();
                    Image noteImage = Image.getInstance(note.getNoteBytes());
                    noteImage.setAlignment(Element.ALIGN_CENTER);
                    noteImage.scaleToFit(280, 1000);
                    noteImage.setBorder(Rectangle.BOX);
                    noteImage.setBorderColor(BaseColor.BLACK);
                    noteImage.setBorderWidth(1f);
                    pNote.add(noteImage);
                    mDoc.add(pNote);
                }
            }
            mDoc.newPage();
            if (!allPatientImagesPath.isEmpty()) {

                Chunk imageTitle = new Chunk("A2: Bilder", fontParagraphTitle);
                Paragraph pImageTitle = new Paragraph(imageTitle);
                mDoc.add(pImageTitle);
                for (String imagePath : allPatientImagesPath) {
                    File file = new File(imagePath);
                    if (file.exists()) {
                        Paragraph pImage = new Paragraph();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        if (bitmap != null) {
                            Image selectedImage = Image.getInstance(bitmapToByte(bitmap));
                            selectedImage.setAlignment(Element.ALIGN_CENTER);
                            selectedImage.scaleToFit(280, 1000);
                            selectedImage.setBorder(Rectangle.BOX);
                            selectedImage.setBorderColor(BaseColor.BLACK);
                            selectedImage.setBorderWidth(1f);
                            pImage.add(selectedImage);
                            mDoc.add(pImage);
                        }

                    }
                }
            }
            mDoc.newPage();
            if (!allPatientWebsites.isEmpty()) {
                Chunk webisiteTitle = new Chunk("A3: Webseiten", fontParagraphTitle);
                Paragraph pWebsiteTitle = new Paragraph(webisiteTitle);
                mDoc.add(pWebsiteTitle);
                if (allPatientWebsites.size() > 1) {
                    mDoc.add(new Paragraph("Für weitere Informationen empfehlen wir Ihnen die folgenden Internetseiten:"));

                } else {
                    mDoc.add(new Paragraph("Für weitere Informationen empfehlen wir Ihnen diese Internetseite:"));
                }
                com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
                for (Integer pw : allPatientWebsites) {
                    for (WebsiteType wt : allWebsiteTypes) {
                        if (wt.getId() == pw) {
                            ListItem listItem = new ListItem();
                            Chunk diagnosisName = new Chunk(wt.getName(), fontListTitle);
                            listItem.add(diagnosisName);
                            if (wt.getUrl() != null && !wt.getUrl().isEmpty()) {
                                Chunk strUrl = new Chunk("\n" + wt.getUrl(), fontListTitle);
                                listItem.add(strUrl);
                            }
                            if (wt.getDescription() != null && !wt.getDescription().isEmpty()) {
                                Chunk strDesc = new Chunk("\n" + wt.getDescription());
                                listItem.add(strDesc);
                            }
                            listItem.setAlignment(Element.ALIGN_JUSTIFIED);
                            list.add(listItem);
                        }
                    }
                }
                mDoc.add(list);
            }


            PdfReader reader = null;
            if (!allPatientDocumentsPath.isEmpty()) {
                for (String documentPath : allPatientDocumentsPath) {
                    File file = new File(documentPath);
                    if (file.exists() && file != null) {
                        byte[] bytesArray = new byte[(int) file.length()];
                        FileInputStream fis = new FileInputStream(file);
                        fis.read(bytesArray); //read file into bytes[]
                        fis.close();
                        reader = new PdfReader(bytesArray);
                        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                            mDoc.newPage();
                            PdfImportedPage page = writer.getImportedPage(reader, i);
                            Image img = Image.getInstance(page);
                            img.scaleToFit(writer.getPageSize());
                            img.setAbsolutePosition((PageSize.A4.getWidth() - img.getScaledWidth()) / 2, (PageSize.A4.getHeight() - img.getScaledHeight()) / 2);
                            mDoc.add(img);
                        }
                    }
                }
            }
            //close the document
            mDoc.close();
            if(reader != null){
                reader.close();
            }

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            File file = new File(filePath);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(Uri.fromFile(file),"application/pdf");
                            target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Intent intent = Intent.createChooser(target, "Open File");
                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Die Zusammenfassung der Konsultation wurde erfolgreich heruntergeladen.").setPositiveButton("Öffnen", dialogClickListener)
                    .setNegativeButton("Beenden", dialogClickListener).show();

        } catch (
                Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void addLineBreak(Document doc, int lines) throws DocumentException {
        for (int i = 0; i < lines; i++) {
            doc.add(new Paragraph(" "));
        }
    }

    private Image createImageWithDrawable(Drawable d) throws IOException, BadElementException {
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        Image image = Image.getInstance(bitmapdata);
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted from popup
                    savePdf();
                } else {
                    //permission was denied from popup, show error message
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            case READ_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted from popup
                    setTitle(getString(R.string.collections));
                    CollectionsFragment collectionsFrag = new CollectionsFragment();
                    collectionsFrag.setArguments(bundlePatientId);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionsFrag).commit();
                    navigationView.setCheckedItem(R.id.nav_collections);
                } else {
                    //permission was denied from popup, show error message
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public byte[] bitmapToByte(Bitmap drawing) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        drawing.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] drawingByteArray = bos.toByteArray();
        return drawingByteArray;
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0)
                getFragmentManager().popBackStack();
            else
                super.onBackPressed();
        }
    }

    public void openClickedFragment(View view) {
        switch (view.getId()) {
            case R.id.btnModel:
                setTitle(getString(R.string.models));
                ModelsFragment modelsFrag = new ModelsFragment();
                modelsFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, modelsFrag).commit();
                navigationView.setCheckedItem(R.id.nav_models);
                break;
            case R.id.btnNotes:
                setTitle(getString(R.string.notes));
                NotesFragment notesFrag = new NotesFragment();
                notesFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, notesFrag).commit();
                navigationView.setCheckedItem(R.id.nav_pen);
                break;
            case R.id.btnCollections:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_STORAGE_CODE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.

                } else {
                    setTitle(getString(R.string.collections));
                    CollectionsFragment collectionsFrag = new CollectionsFragment();
                    collectionsFrag.setArguments(bundlePatientId);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, collectionsFrag).commit();
                    navigationView.setCheckedItem(R.id.nav_collections);
                }
                break;
            case R.id.btnPsychosocial:
                setTitle(getString(R.string.psychosocial));
                PsychosocialFragment psychosocialFrag = new PsychosocialFragment();
                psychosocialFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, psychosocialFrag).commit();
                navigationView.setCheckedItem(R.id.nav_psychosocial);
                break;
                /*
            case R.id.btnRecognizer:
                setTitle(getString(R.string.recognizer));
                RecognizerFragment recognizerFrag = new RecognizerFragment();
                recognizerFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recognizerFrag).commit();
                navigationView.setCheckedItem(R.id.nav_recognizer);
                break;
                */
            case R.id.btnTranslator:
                setTitle(getString(R.string.translator));
                TranslatorFragment translatorFrag = new TranslatorFragment();
                translatorFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, translatorFrag).commit();
                navigationView.setCheckedItem(R.id.nav_translator);
                break;
            case R.id.btnPain:
                setTitle(getString(R.string.paindetails));
                PainFragment painFrag = new PainFragment();
                painFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, painFrag).commit();
                navigationView.setCheckedItem(R.id.nav_pain);
                break;
            case R.id.btnDiagnosis:
                setTitle(getString(R.string.diagnosis));
                DiagnosisFragment diagnosisFrag = new DiagnosisFragment();
                diagnosisFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, diagnosisFrag).commit();
                navigationView.setCheckedItem(R.id.nav_diagnosis);
                break;
            case R.id.btnDrugs:
                setTitle(getString(R.string.drugs));
                DrugsFragment drugsFrag = new DrugsFragment();
                drugsFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, drugsFrag).commit();
                navigationView.setCheckedItem(R.id.nav_drugs);
                break;
            case R.id.btnExercises:
                setTitle(getString(R.string.exercises));
                ExercisesFragment exercisesFrag = new ExercisesFragment();
                exercisesFrag.setArguments(bundlePatientId);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exercisesFrag).commit();
                navigationView.setCheckedItem(R.id.nav_exercises);
                break;
            case R.id.btnSummary:
                //handle runtime permission for devices with marshmallow and above
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    //system OS >= Marshmallow (6.0), check if permission is enabled or not
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission was not granted, request it
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        //permission already granted
                        createPdf();
                    }
                } else {
                    //system OS < Marshmallow no required to check runtime permission
                }
                break;
        }

    }


    public void navigateBackToPatients(View view) {
        setTitle("Patienten");
        Intent intent = new Intent(MenuActivity.this, PatientSelectionActivity.class);
        startActivity(intent);
    }

    public void getPatientNotes() {
        allNotesOfPatient = db.getSelectedNotesOfPatient(patient.getId());
        getExercisePhotos();

        /*Only used for Heruoku Database

        Call<List<Note>> call = jsonPlaceHolderApi.getSelectedNotesOfPatient(patient.getId());
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "not succesful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allNotesOfPatient = response.body();
                    getExercisePhotos();
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        }); */
    }

    public void getPatientDiagnoses() {
        //Get all PatientDiagnoses of Patient
        allPatientDiagnoses = db.getAllDiagnosesOfPatient(patient.getId());
        getAllDiagnosisTypes();

        /*Only used for Heruoku Database

        Call<List<PatientDiagnosis>> call = jsonPlaceHolderApi.getPatientDiagnoses(patient.getId());
        call.enqueue(new Callback<List<PatientDiagnosis>>() {
            @Override
            public void onResponse(Call<List<PatientDiagnosis>> call, Response<List<PatientDiagnosis>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "GetPatientDiagnosesOfClass not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allPatientDiagnoses = response.body();
                    getAllDiagnosisTypes();
                }
            }

            @Override
            public void onFailure(Call<List<PatientDiagnosis>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void getAllDiagnosisTypes() {
        //Get all DiagnosisTypes of Patient
        allDiagnosisTypes = db.getAllDiagnosisTypes();
        getPatientNotes();

        /*Only used for Heruoku Database
        Call<List<DiagnosisType>> call = jsonPlaceHolderApi.getAllDiagnosisTypes();
        call.enqueue(new Callback<List<DiagnosisType>>() {
            @Override
            public void onResponse(Call<List<DiagnosisType>> call, Response<List<DiagnosisType>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "Get DiagnosisTypes not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allDiagnosisTypes = response.body();
                    getPatientNotes();
                }
            }

            @Override
            public void onFailure(Call<List<DiagnosisType>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void getPatientDrugs() {
        allPatientDrugs = db.getAllDrugsOfPatient(patient.getId());

        //Get all PatientDrugs of Patient

        /*Only used for Heruoku Database
        Call<List<PatientDrug>> call = jsonPlaceHolderApi.getAllDrugsOfPatient(patient.getId());
        call.enqueue(new Callback<List<PatientDrug>>() {
            @Override
            public void onResponse(Call<List<PatientDrug>> call, Response<List<PatientDrug>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "GetPatientDrugsOfClass not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allPatientDrugs = response.body();

                }
            }

            @Override
            public void onFailure(Call<List<PatientDrug>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void getAllDrugTypes() {
        //Get all DrugTypes of Patient
        allDrugTypes = db.getAllDrugTypes();

        /*Only used for Heruoku Database
        Call<List<DrugType>> call = jsonPlaceHolderApi.getAllDrugTypes();
        call.enqueue(new Callback<List<DrugType>>() {
            @Override
            public void onResponse(Call<List<DrugType>> call, Response<List<DrugType>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "Get DrugTypes not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allDrugTypes = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<DrugType>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }


    public void getExercisePhotos() {
        allExercisPhotos = db.getAllExercisePhotosOfPatient(patient.getId());
        savePdf();

        /*Only used for Heruoku Database

        Call<List<ExercisePhoto>> call = jsonPlaceHolderApi.getExercisePhotosOfPatient(patient.getId());
        call.enqueue(new Callback<List<ExercisePhoto>>() {
            @Override
            public void onResponse(Call<List<ExercisePhoto>> call, Response<List<ExercisePhoto>> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    allExercisPhotos = response.body();
                    savePdf();
                }
            }

            @Override
            public void onFailure(Call<List<ExercisePhoto>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }); */
    }

    public void getPatientExercises() {
        allPatientExercises = db.getAllExercisesOfPatient(patient.getId());

        /*Only used for Heruoku Database

        Call<List<PatientExercise>> call = jsonPlaceHolderApi.getSelectedPatientExercises(patient.getId());
        call.enqueue(new Callback<List<PatientExercise>>() {
            @Override
            public void onResponse(Call<List<PatientExercise>> call, Response<List<PatientExercise>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "not succesful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allPatientExercises = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<PatientExercise>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }); */
    }

    public void getAllExerciseTypes() {
        //Get all ExerciseTypes of Patient
        allExerciseTypes = db.getAllExerciseTypes();

        /*Only used for Heruoku Database

        Call<List<ExerciseType>> call = jsonPlaceHolderApi.getAllExerciseTypes();
        call.enqueue(new Callback<List<ExerciseType>>() {
            @Override
            public void onResponse(Call<List<ExerciseType>> call, Response<List<ExerciseType>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "Get ExerciseTypes not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allExerciseTypes = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseType>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void getAllWebsiteTypes() {
        //Get all WebsiteTypes of Patient
        allWebsiteTypes = db.getAllWebsiteTypes();

        /*Only used for Heruoku Database

        Call<List<WebsiteType>> call = jsonPlaceHolderApi.getAllWebsiteTypes();
        call.enqueue(new Callback<List<WebsiteType>>() {
            @Override
            public void onResponse(Call<List<WebsiteType>> call, Response<List<WebsiteType>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "Get WebsiteTypes not successful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allWebsiteTypes = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<WebsiteType>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    public void getPatientWebsites() {
        allPatientWebsites = db.getAllWebsiteIdsOfPatient(patient.getId());

        /*Only used for Heruoku Database

        Call<List<Integer>> call = jsonPlaceHolderApi.getAllWebsiteIdsOfPatient(patient.getId());
        call.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "not succesful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allPatientWebsites = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }); */
    }

    public void getPatientImages() {
        allPatientImagesPath = db.getAllImagePathsOfPatient(patient.getId());

        /*Only used for Heruoku Database

        Call<List<String>> call = jsonPlaceHolderApi.getAllImagePathsOfPatient(patient.getId());
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "not succesful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allPatientImagesPath = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }); */
    }

    public void getPatientDocuments() {
        allPatientDocumentsPath = db.getAllDocumentPathsOfPatient(patient.getId());

        /*Only used for Heruoku Database

        Call<List<String>> call = jsonPlaceHolderApi.getAllDocumentPathsOfPatient(patient.getId());
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "not succesful", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    allPatientDocumentsPath = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }); */
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
