package com.example.frontend.Service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.frontend.Models.DiagnosisType;
import com.example.frontend.Models.DrugType;
import com.example.frontend.Models.ExercisePhoto;
import com.example.frontend.Models.ExerciseType;
import com.example.frontend.Models.ImprovementReason;
import com.example.frontend.Models.Note;
import com.example.frontend.Models.PainBeginning;
import com.example.frontend.Models.PainCurrent;
import com.example.frontend.Models.Patient;
import com.example.frontend.Models.PatientDiagnosis;
import com.example.frontend.Models.PatientDocument;
import com.example.frontend.Models.PatientDrug;
import com.example.frontend.Models.PatientExercise;
import com.example.frontend.Models.PatientImage;
import com.example.frontend.Models.PatientVideo;
import com.example.frontend.Models.PatientWebsite;
import com.example.frontend.Models.PsychoSocialAfter;
import com.example.frontend.Models.PsychoSocialBefore;
import com.example.frontend.Models.User;
import com.example.frontend.Models.WebsiteType;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.android.ContextHolder;
import org.sqldroid.DroidDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Pacons_DB";

    private Context c;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {

        createTables(db);

        try {
            //Insert default data into database tables
            insertFromFile("V1__PatientTable.sql", db);
            insertFromFile("V2__DrugTables.sql", db);
            insertFromFile("V3__DiagnosisTables.sql", db);
            insertFromFile("V4__ExerciseTables.sql", db);
            insertFromFile("V5__WebsiteTable.sql", db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Patient");
        db.execSQL("DROP TABLE IF EXISTS DrugType");
        db.execSQL("DROP TABLE IF EXISTS PatientDrug");
        db.execSQL("DROP TABLE IF EXISTS DiagnosisType");
        db.execSQL("DROP TABLE IF EXISTS PatientDiagnosis");
        db.execSQL("DROP TABLE IF EXISTS ExerciseType");
        db.execSQL("DROP TABLE IF EXISTS PatientExercise");
        db.execSQL("DROP TABLE IF EXISTS ExercisePhoto");
        db.execSQL("DROP TABLE IF EXISTS Note");
        db.execSQL("DROP TABLE IF EXISTS PsychoSocialBefore");
        db.execSQL("DROP TABLE IF EXISTS PsychoSocialAfter");
        db.execSQL("DROP TABLE IF EXISTS ImprovementReason");
        db.execSQL("DROP TABLE IF EXISTS PatientImage");
        db.execSQL("DROP TABLE IF EXISTS PatientVideo");
        db.execSQL("DROP TABLE IF EXISTS PatientDocument");
        db.execSQL("DROP TABLE IF EXISTS WebsiteType");
        db.execSQL("DROP TABLE IF EXISTS PatientWebsite");
        db.execSQL("DROP TABLE IF EXISTS PainBeginning");
        db.execSQL("DROP TABLE IF EXISTS PainCurrent");

        // Create tables again
        onCreate(db);
    }

    public void createTables(SQLiteDatabase db) {

        //User Table
        db.execSQL("CREATE TABLE User (" +
                "id INTEGER PRIMARY KEY, " +
                "username text, " +
                "password TEXT)");

        //Patient Table
        db.execSQL("CREATE TABLE Patient ( " +
                "    id INTEGER PRIMARY KEY, " +
                "    shortname text NOT NULL, " +
                "    gender text NOT NULL " +
                ")");

        //Drug Tables
        db.execSQL("CREATE TABLE DrugType ( " +
                "    id INTEGER PRIMARY KEY, " +
                "    name text NOT NULL, " +
                "    description text " +
                ")");
        db.execSQL("CREATE TABLE PatientDrug ( " +
                "    patient_id int NOT NULL, " +
                "    drugtype_id int NOT NULL, " +
                "    amount text, " +
                "    dosis char(4), " +
                "    comment text, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE CASCADE, " +
                "    FOREIGN KEY (drugtype_id) REFERENCES DrugType(id) ON DELETE CASCADE, " +
                "    UNIQUE (patient_id, drugtype_id) " +
                ")");

        //Diagnosis Tables
        db.execSQL("CREATE TABLE DiagnosisType ( " +
                "    id INTEGER PRIMARY KEY, " +
                "    name text NOT NULL, " +
                "    type text NOT NULL, " +
                "    description text " +
                ")");
        db.execSQL("CREATE TABLE PatientDiagnosis ( " +
                "    patient_id int NOT NULL, " +
                "    diagnosistype_id int NOT NULL, " +
                "    comment text, " +
                "    priority int, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE CASCADE, " +
                "    FOREIGN KEY (diagnosistype_id) REFERENCES DiagnosisType(id) ON DELETE CASCADE, " +
                "    UNIQUE (patient_id, diagnosistype_id) " +
                ")");

        //Exercise Tables
        db.execSQL(" CREATE TABLE ExerciseType ( " +
                "    title text PRIMARY KEY, " +
                "    explanation text " +
                ")");
        db.execSQL("CREATE TABLE PatientExercise ( " +
                "    patient_id int NOT NULL, " +
                "    exercisetype_title text NOT NULL, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE CASCADE, " +
                "    FOREIGN KEY (exercisetype_title) REFERENCES ExerciseType(title) ON DELETE CASCADE, " +
                "    UNIQUE (patient_id, exercisetype_title) " +
                ")");
        db.execSQL("CREATE TABLE ExercisePhoto ( " +
                "    id INTEGER PRIMARY KEY, " +
                "    patient_id int NOT NULL, " +
                "    photo BLOB NOT NULL, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE CASCADE " +
                ")");

        //Note Table
        db.execSQL("CREATE TABLE Note ( " +
                "    id INTEGER PRIMARY KEY, " +
                "    patient_id int NOT NULL, " +
                "    note_bytes BLOB NOT NULL, " +
                "    selected boolean, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient(id) ON DELETE CASCADE " +
                ")");

        //Psychosocial Factors Tables
        db.execSQL("CREATE TABLE PsychoSocialBefore " +
                "( " +
                "    patient_id   int PRIMARY KEY, " +
                "    pain_xpos    int NOT NULL, " +
                "    pain_ypos    int NOT NULL, " +
                "    pain_color   int NOT NULL, " +
                "    pain_size    int NOT NULL, " +
                "    family_xpos int NOT NULL, " +
                "    family_ypos int NOT NULL, " +
                "    family_color int NOT NULL, " +
                "    family_size  int NOT NULL, " +
                "    work_xpos    int NOT NULL, " +
                "    work_ypos    int NOT NULL, " +
                "    work_color   text NOT NULL, " +
                "    work_size    int NOT NULL, " +
                "    finance_xpos int NOT NULL, " +
                "    finance_ypos int NOT NULL, " +
                "    finance_color  int NOT NULL, " +
                "    finance_size int NOT NULL, " +
                "    event_xpos   int NOT NULL, " +
                "    event_ypos   int NOT NULL, " +
                "    event_color  int NOT NULL, " +
                "    event_size   int NOT NULL, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE " +
                ")");
        db.execSQL("CREATE TABLE PsychoSocialAfter " +
                "( " +
                "    patient_id   int PRIMARY KEY, " +
                "    pain_xpos    int NOT NULL, " +
                "    pain_ypos    int NOT NULL, " +
                "    pain_color   int NOT NULL, " +
                "    pain_size    int NOT NULL, " +
                "    family_xpos int NOT NULL, " +
                "    family_ypos int NOT NULL, " +
                "    family_color int NOT NULL, " +
                "    family_size  int NOT NULL, " +
                "    work_xpos    int NOT NULL, " +
                "    work_ypos    int NOT NULL, " +
                "    work_color   text NOT NULL, " +
                "    work_size    int NOT NULL, " +
                "    finance_xpos int NOT NULL, " +
                "    finance_ypos int NOT NULL, " +
                "    finance_color  int NOT NULL, " +
                "    finance_size int NOT NULL, " +
                "    event_xpos   int NOT NULL, " +
                "    event_ypos   int NOT NULL, " +
                "    event_color  int NOT NULL, " +
                "    event_size   int NOT NULL, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE " +
                ")");
        db.execSQL("CREATE TABLE ImprovementReason " +
                "( " +
                "    patient_id   int PRIMARY KEY, " +
                "    drugs boolean, " +
                "    exercises boolean, " +
                "    awareness boolean, " +
                "    other_reason boolean, " +
                "    other_reason_text text, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE " +
                ")");

        //Media Tables
        db.execSQL("CREATE TABLE PatientImage " +
                "( " +
                "    patient_id int  NOT NULL, " +
                "    image_path text NOT NULL, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE, " +
                "    UNIQUE (patient_id, image_path) " +
                ")");
        db.execSQL("CREATE TABLE PatientVideo " +
                "( " +
                "    patient_id int  NOT NULL, " +
                "    video_path text NOT NULL, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE, " +
                "    UNIQUE (patient_id, video_path) " +
                ")");

        db.execSQL("CREATE TABLE PatientDocument " +
                "( " +
                "    patient_id int NOT NULL, " +
                "    document_path text NOT NULL, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE, " +
                "    UNIQUE (patient_id, document_path) " +
                ")");
        db.execSQL("CREATE TABLE WebsiteType " +
                "( " +
                "    id INTEGER PRIMARY KEY, " +
                "    name text NOT NULL, " +
                "    url text NOT NULL, " +
                "    description text " +
                ")");
        db.execSQL("CREATE TABLE PatientWebsite " +
                "( " +
                "    patient_id int  NOT NULL, " +
                "    website_id int NOT NULL, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE, " +
                "    FOREIGN KEY (website_id) REFERENCES WebsiteType (id) ON DELETE CASCADE, " +
                "    UNIQUE (patient_id, website_id) " +
                ")");

        //Pain Description Tables
        db.execSQL("CREATE TABLE PainBeginning " +
                "( " +
                "    patient_id int  PRIMARY KEY, " +
                "    intensity int, " +
                "    location_teeth BLOB, " +
                "    location_face_left BLOB, " +
                "    location_face_right BLOB, " +
                "    pain_pattern text, " +
                "    dull boolean, " +
                "    pulling boolean, " +
                "    stinging boolean, " +
                "    pulsating boolean, " +
                "    burning boolean, " +
                "    pinsneedles boolean, " +
                "    tingling boolean, " +
                "    numb boolean, " +
                "    comment text," +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE " +
                ")");
        db.execSQL("CREATE TABLE PainCurrent " +
                "( " +
                "    patient_id int  PRIMARY KEY, " +
                "    intensity int, " +
                "    location_teeth BLOB, " +
                "    location_face_left BLOB, " +
                "    location_face_right BLOB, " +
                "    pain_pattern text, " +
                "    dull boolean, " +
                "    pulling boolean, " +
                "    stinging boolean, " +
                "    pulsating boolean, " +
                "    burning boolean, " +
                "    pinsneedles boolean, " +
                "    tingling boolean, " +
                "    numb boolean," +
                "    comment text, " +
                "    FOREIGN KEY (patient_id) REFERENCES Patient (id) ON DELETE CASCADE " +
                ")");
    }

    public void insertFromFile(String fileName, SQLiteDatabase db) throws IOException {
        // Open the resource
        InputStream insertsStream = c.getAssets().open(fileName);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            db.execSQL(insertStmt);
        }
        insertReader.close();
    }


    //User table related functions

    public void createDefaultUsersIfNeed() {
        int count = this.getUsersCount();
        if (count == 0) {
            User user1 = new User("admin",
                    "zzm");
            User user2 = new User("de",
                    "zzm");
            User user3 = new User("nl",
                    "zzm");
            User user4 = new User("aw",
                    "zzm");
            this.addUser(user1);
            this.addUser(user2);
            this.addUser(user3);
            this.addUser(user4);
        }
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());

        // Inserting Row
        db.insert("User", null, values);

        // Closing database connection
        db.close();
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("User", new String[]{"id",
                        "username", "password"}, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return user
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT * FROM User";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return user list
        return userList;
    }

    public int getUsersCount() {
        String countQuery = "SELECT * FROM User";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());

        // updating row
        return db.update("User", values, "id = ?",
                new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("User", "id = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }


    //Patient table related functions

    public void addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("shortname", patient.getShortname());
        values.put("gender", patient.getGender());

        // Inserting Row
        db.insert("Patient", null, values);

        // Closing database connection
        db.close();
    }

    public Patient getPatient(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Patient", new String[]{"id",
                        "shortname", "gender"}, "id = ?",
                new String[]{String.valueOf(patientId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Patient patient = new Patient(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return patient
        return patient;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patientList = new ArrayList<Patient>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Patient ORDER BY shortname";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setId(Integer.parseInt(cursor.getString(0)));
                patient.setShortname(cursor.getString(1));
                patient.setGender(cursor.getString(2));
                // Adding patient to list
                patientList.add(patient);
            } while (cursor.moveToNext());
        }

        // return patient list
        return patientList;
    }

    public int updatePatient(int patientId, Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("shortname", patient.getShortname());
        values.put("gender", patient.getGender());

        // updating row
        return db.update("Patient", values, "id = ?",
                new String[]{String.valueOf(patientId)});
    }

    public void deletePatient(int patientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Patient", "id = ?",
                new String[]{String.valueOf(patientId)});
        db.close();
    }

    public int selectLastPatientId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Patient", new String[]{"id",
                "shortname", "gender"}, null, null, null, null, null);
        cursor.moveToLast();
        int lastId = Integer.parseInt(cursor.getString(0));
        return lastId;
    }


    //DrugType table related functions

    public void addDrugType(DrugType drugType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", drugType.getName());
        values.put("description", drugType.getDescription());

        // Inserting Row
        db.insert("DrugType", null, values);

        // Closing database connection
        db.close();
    }

    public DrugType getDrugType(int drugTypeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("DrugType", new String[]{"id",
                        "name", "description"}, "id = ?",
                new String[]{String.valueOf(drugTypeId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DrugType drugType = new DrugType(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return drugType
        return drugType;
    }

    public List<DrugType> getAllDrugTypes() {
        List<DrugType> drugTypeList = new ArrayList<DrugType>();
        // Select All Query
        String selectQuery = "SELECT * FROM DrugType ORDER BY name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DrugType drugType = new DrugType();
                drugType.setId(Integer.parseInt(cursor.getString(0)));
                drugType.setName(cursor.getString(1));
                drugType.setDescription(cursor.getString(2));
                // Adding drugType to list
                drugTypeList.add(drugType);
            } while (cursor.moveToNext());
        }

        // return drugType list
        return drugTypeList;
    }

    public int updateDrugType(int drugTypeId, DrugType drugType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", drugType.getName());
        values.put("description", drugType.getDescription());

        // updating row
        return db.update("DrugType", values, "id = ?",
                new String[]{String.valueOf(drugTypeId)});
    }

    public void deleteDrugType(int drugTypeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("DrugType", "id = ?",
                new String[]{String.valueOf(drugTypeId)});
        db.close();
    }

    public int selectLastDrugTypeId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("DrugType", new String[]{"id",
                "name", "description"}, null, null, null, null, null);
        cursor.moveToLast();
        int lastId = Integer.parseInt(cursor.getString(0));
        return lastId;
    }


    //PatientDrug table related functions

    public void addPatientDrug(PatientDrug patientDrug) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientDrug.getPatientId());
        values.put("drugtype_id", patientDrug.getDrugTypeId());
        values.put("amount", patientDrug.getAmount());
        values.put("dosis", patientDrug.getDosis());
        values.put("comment", patientDrug.getComment());

        // Inserting Row
        db.insert("PatientDrug", null, values);

        // Closing database connection
        db.close();
    }

    public PatientDrug getPatientDrug(int patientId, int drugTypeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("PatientDrug", new String[]{"patient_id", "drugtype_id",
                        "amount", "dosis", "comment"}, "patient_id = ? AND drugtype_id = ?",
                new String[]{String.valueOf(patientId), String.valueOf(drugTypeId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PatientDrug patientDrug = new PatientDrug(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return patientDrug
        return patientDrug;
    }

    public List<PatientDrug> getAllPatientDrugs() {
        List<PatientDrug> patientDrugList = new ArrayList<PatientDrug>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientDrug ORDER BY name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientDrug patientDrug = new PatientDrug();
                patientDrug.setPatientId(Integer.parseInt(cursor.getString(0)));
                patientDrug.setDrugId(Integer.parseInt(cursor.getString(1)));
                patientDrug.setAmount(cursor.getString(2));
                patientDrug.setDosis(cursor.getString(3));
                patientDrug.setComment(cursor.getString(4));
                // Adding patientDrug to list
                patientDrugList.add(patientDrug);
            } while (cursor.moveToNext());
        }

        // return patientDrug list
        return patientDrugList;
    }

    public List<PatientDrug> getAllDrugsOfPatient(int patientId) {
        List<PatientDrug> drugsOfPatientList = new ArrayList<PatientDrug>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientDrug WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientDrug patientDrug = new PatientDrug();
                patientDrug.setPatientId(patientId);
                patientDrug.setDrugId(Integer.parseInt(cursor.getString(1)));
                patientDrug.setAmount(cursor.getString(2));
                patientDrug.setDosis(cursor.getString(3));
                patientDrug.setComment(cursor.getString(4));
                // Adding patientDrug to list
                drugsOfPatientList.add(patientDrug);
            } while (cursor.moveToNext());
        }

        // return patientDrug list
        return drugsOfPatientList;
    }

    public int updatePatientDrug(int patientId, int drugTypeId, PatientDrug patientDrug) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientDrug.getPatientId());
        values.put("drugtype_id", patientDrug.getDrugTypeId());
        values.put("amount", patientDrug.getAmount());
        values.put("dosis", patientDrug.getDosis());
        values.put("comment", patientDrug.getComment());

        // updating row
        return db.update("PatientDrug", values, "patient_id = ? AND drugtype_id= ?",
                new String[]{String.valueOf(patientId), String.valueOf(drugTypeId)});
    }

    public void deletePatientDrug(int patientId, int drugTypeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PatientDrug", "patient_id = ? AND drugtype_id = ?",
                new String[]{String.valueOf(patientId), String.valueOf(drugTypeId)});
        db.close();
    }


    //DiagnosisType table related functions

    public void addDiagnosisType(DiagnosisType diagnosisType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", diagnosisType.getName());
        values.put("type", diagnosisType.getType());
        values.put("description", diagnosisType.getDescription());

        // Inserting Row
        db.insert("DiagnosisType", null, values);

        // Closing database connection
        db.close();
    }

    public DiagnosisType getDiagnosisType(int diagnosisTypeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("DiagnosisType", new String[]{"id",
                        "name", "type", "description"}, "id = ?",
                new String[]{String.valueOf(diagnosisTypeId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DiagnosisType diagnosisType = new DiagnosisType(
                Integer.parseInt(cursor.getString(0)), //id
                cursor.getString(1), //name
                cursor.getString(2), //type
                cursor.getString(3)); //description
        // return diagnosisType
        return diagnosisType;
    }

    public List<DiagnosisType> getAllDiagnosisTypes() {
        List<DiagnosisType> diagnosisTypeList = new ArrayList<DiagnosisType>();
        // Select All Query
        String selectQuery = "SELECT * FROM DiagnosisType ORDER BY name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DiagnosisType diagnosisType = new DiagnosisType();
                diagnosisType.setId(Integer.parseInt(cursor.getString(0)));
                diagnosisType.setName(cursor.getString(1));
                diagnosisType.setType(cursor.getString(2));
                diagnosisType.setDescription(cursor.getString(3));
                // Adding diagnosisType to list
                diagnosisTypeList.add(diagnosisType);
            } while (cursor.moveToNext());
        }

        // return diagnosisType list
        return diagnosisTypeList;
    }

    public List<DiagnosisType> getDiagnosisTypesByClass(String type) {
        List<DiagnosisType> diagnosisTypesOfClass = new ArrayList<DiagnosisType>();
        // Select All Query
        String selectQuery = "SELECT * FROM DiagnosisType WHERE type = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{type});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DiagnosisType diagnosisType = new DiagnosisType();
                diagnosisType.setId(Integer.parseInt(cursor.getString(0)));
                diagnosisType.setName(cursor.getString(1));
                diagnosisType.setType(type);
                diagnosisType.setDescription(cursor.getString(3));
                // Adding diagnosisType to list
                diagnosisTypesOfClass.add(diagnosisType);
            } while (cursor.moveToNext());
        }

        // return patientDrug list
        return diagnosisTypesOfClass;
    }

    public List<String> getAllDiagnosisTypeClasses() {
        List<String> allDiagnosisTypeClasses = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT type FROM DiagnosisType ORDER BY type";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding class to list
                allDiagnosisTypeClasses.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        // return class list
        return allDiagnosisTypeClasses;
    }

    public int updateDiagnosisType(int diagnosisTypeId, DiagnosisType diagnosisType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", diagnosisType.getName());
        values.put("type", diagnosisType.getType());
        values.put("description", diagnosisType.getDescription());

        // updating row
        return db.update("DiagnosisType", values, "id = ?",
                new String[]{String.valueOf(diagnosisTypeId)});
    }

    public void deleteDiagnosisType(int diagnosisTypeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("DiagnosisType", "id = ?",
                new String[]{String.valueOf(diagnosisTypeId)});
        db.close();
    }

    public int selectLastDiagnosisTypeId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("DiagnosisType", new String[]{"id",
                "name", "type", "description"}, null, null, null, null, null);
        cursor.moveToLast();
        int lastId = Integer.parseInt(cursor.getString(0));
        return lastId;
    }


    //PatientDiagnosis table related functions

    public void addPatientDiagnosis(PatientDiagnosis patientDiagnosis) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientDiagnosis.getPatientId());
        values.put("diagnosistype_id", patientDiagnosis.getDiagnosisId());
        values.put("comment", patientDiagnosis.getComment());
        values.put("priority", patientDiagnosis.getPriority());

        // Inserting Row
        db.insert("PatientDiagnosis", null, values);

        // Closing database connection
        db.close();
    }

    public PatientDiagnosis getPatientDiagnosis(int patientId, int diagnosisTypeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("PatientDiagnosis", new String[]{"patient_id", "diagnosistype_id",
                        "comment", "priority"}, "patient_id = ? AND diagnosistype_id = ?",
                new String[]{String.valueOf(patientId), String.valueOf(diagnosisTypeId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PatientDiagnosis patientDiagnosis = new PatientDiagnosis(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        // return patientDiagnosis
        return patientDiagnosis;
    }

    public List<PatientDiagnosis> getAllPatientDiagnoses() {
        List<PatientDiagnosis> patientDiagnosisList = new ArrayList<PatientDiagnosis>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientDiagnosis ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
                patientDiagnosis.setPatientId(Integer.parseInt(cursor.getString(0)));
                patientDiagnosis.setDiagnosisId(Integer.parseInt(cursor.getString(1)));
                patientDiagnosis.setComment(cursor.getString(2));
                if (cursor.getString(3) == null){
                    patientDiagnosis.setPriority(-1);
                }else{
                    patientDiagnosis.setPriority(Integer.parseInt(cursor.getString(3)));
                }                // Adding patientDiagnosis to list
                patientDiagnosisList.add(patientDiagnosis);
            } while (cursor.moveToNext());
        }

        // return patientDiagnosis list
        return patientDiagnosisList;
    }

    public List<PatientDiagnosis> getAllDiagnosesOfPatient(int patientId) {
        List<PatientDiagnosis> diagnosesOfPatientList = new ArrayList<PatientDiagnosis>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientDiagnosis WHERE patient_id = ? ORDER BY priority";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
                patientDiagnosis.setPatientId(patientId);
                patientDiagnosis.setDiagnosisId(Integer.parseInt(cursor.getString(1)));
                patientDiagnosis.setComment(cursor.getString(2));
                if (cursor.getString(3) == null){
                    patientDiagnosis.setPriority(-1);
                }else{
                    patientDiagnosis.setPriority(Integer.parseInt(cursor.getString(3)));
                }
                // Adding patientDiagnosis to list
                diagnosesOfPatientList.add(patientDiagnosis);
            } while (cursor.moveToNext());
        }

        // return patientDiagnosis list
        return diagnosesOfPatientList;
    }

    public List<PatientDiagnosis> getPatientDiagnosisOfClass(int patientId, String type) {
        List<PatientDiagnosis> selectedPatientDiagnoses = new ArrayList<PatientDiagnosis>();
        // Select All Query
        String selectQuery = "SELECT patient_id, diagnosistype_id, comment, priority FROM PatientDiagnosis " +
                "INNER JOIN DiagnosisType " +
                "ON PatientDiagnosis.diagnosistype_id =  DiagnosisType.id " +
                "WHERE PatientDiagnosis.patient_id = ? AND DiagnosisType.type = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId), type});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
                patientDiagnosis.setPatientId(patientId);
                patientDiagnosis.setDiagnosisId(Integer.parseInt(cursor.getString(1)));
                patientDiagnosis.setComment(cursor.getString(2));
                if (cursor.getString(3) == null){
                    patientDiagnosis.setPriority(-1);
                }else{
                    patientDiagnosis.setPriority(Integer.parseInt(cursor.getString(3)));
                }

                // Adding patientDiagnosis to list
                selectedPatientDiagnoses.add(patientDiagnosis);
            } while (cursor.moveToNext());
        }

        // return patientDiagnosisList
        return selectedPatientDiagnoses;
    }

    public int updatePatientDiagnosis(int patientId, int diagnosisTypeId, PatientDiagnosis patientDiagnosis) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientId);
        values.put("diagnosistype_id", diagnosisTypeId);
        values.put("comment", patientDiagnosis.getComment());
        values.put("priority", patientDiagnosis.getPriority());

        // updating row
        return db.update("PatientDiagnosis", values, "patient_id = ? AND diagnosistype_id= ?",
                new String[]{String.valueOf(patientId), String.valueOf(diagnosisTypeId)});
    }

    public void deletePatientDiagnosis(int patientId, int diagnosisTypeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PatientDiagnosis", "patient_id = ? AND diagnosistype_id = ?",
                new String[]{String.valueOf(patientId), String.valueOf(diagnosisTypeId)});
        db.close();
    }


    //ExerciseType table related functions

    public void addExerciseType(ExerciseType exerciseType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", exerciseType.getTitle());
        values.put("explanation", exerciseType.getExplanation());

        // Inserting Row
        db.insert("ExerciseType", null, values);

        // Closing database connection
        db.close();
    }

    public List<ExerciseType> getAllExerciseTypes() {
        List<ExerciseType> exerciseTypeList = new ArrayList<ExerciseType>();
        // Select All Query
        String selectQuery = "SELECT * FROM ExerciseType ORDER BY title";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ExerciseType exerciseType = new ExerciseType();
                exerciseType.setTitle(cursor.getString(0));
                exerciseType.setExplanation(cursor.getString(1));
                // Adding exerciseType to list
                exerciseTypeList.add(exerciseType);
            } while (cursor.moveToNext());
        }

        // return exerciseType list
        return exerciseTypeList;
    }

    public ExerciseType getExerciseType(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("ExerciseType", new String[]{"title",
                        "explanation"}, "title = ?",
                new String[]{title}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ExerciseType exerciseType = new ExerciseType(
                title, //title
                cursor.getString(1)); //explanation
        // return exerciseType
        return exerciseType;
    }

    public List<String> getAllExerciseTypeTitles() {
        List<String> allExerciseTypeTitles = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT title FROM ExerciseType ORDER BY title";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding title to list
                allExerciseTypeTitles.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        // return title list
        return allExerciseTypeTitles;
    }

    public int updateExerciseType(String title, ExerciseType exerciseType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("explanation", exerciseType.getExplanation());

        // updating row
        return db.update("ExerciseType", values, "title = ?",
                new String[]{String.valueOf(title)});
    }

    public void deleteExerciseType(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ExerciseType", "title = ?",
                new String[]{String.valueOf(title)});
        db.close();
    }


    //PatientExercise table related functions

    public void addPatientExercise(PatientExercise patientExercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientExercise.getPatientId());
        values.put("exercisetype_title", patientExercise.getExerciseTypeTitle());

        // Inserting Row
        db.insert("PatientExercise", null, values);

        // Closing database connection
        db.close();
    }

    public List<PatientExercise> getAllPatientExercises() {
        List<PatientExercise> patientExerciseList = new ArrayList<PatientExercise>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientExercise ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientExercise patientExercise = new PatientExercise();
                patientExercise.setPatientId(Integer.parseInt(cursor.getString(0)));
                patientExercise.setExerciseTypeTitle(cursor.getString(1));
                // Adding patientExercise to list
                patientExerciseList.add(patientExercise);
            } while (cursor.moveToNext());
        }

        // return patientExercise list
        return patientExerciseList;
    }

    public List<PatientExercise> getAllExercisesOfPatient(int patientId) {
        List<PatientExercise> exercisesOfPatientList = new ArrayList<PatientExercise>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientExercise WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientExercise patientExercise = new PatientExercise();
                patientExercise.setPatientId(patientId);
                patientExercise.setExerciseTypeTitle(cursor.getString(1));
                // Adding patientExercise to list
                exercisesOfPatientList.add(patientExercise);
            } while (cursor.moveToNext());
        }

        // return patientExercise list
        return exercisesOfPatientList;
    }

    public void deletePatientExercise(int patientId, String exercisetypeTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PatientExercise", "patient_id = ? AND exercisetype_title = ?",
                new String[]{String.valueOf(patientId), exercisetypeTitle});
        db.close();
    }


    //ExercisePhoto table related functions

    public void addExercisePhoto(ExercisePhoto patientExercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientExercise.getPatientId());
        values.put("photo", patientExercise.getPhotoBytes());

        // Inserting Row
        db.insert("ExercisePhoto", null, values);

        // Closing database connection
        db.close();
    }

    public List<ExercisePhoto> getAllExercisePhotos() {
        List<ExercisePhoto> patientExerciseList = new ArrayList<ExercisePhoto>();
        // Select All Query
        String selectQuery = "SELECT * FROM ExercisePhoto ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ExercisePhoto patientExercise = new ExercisePhoto();
                patientExercise.setId(Integer.parseInt(cursor.getString(0)));
                patientExercise.setPatientId(Integer.parseInt(cursor.getString(1)));
                patientExercise.setPhotoBytes(cursor.getBlob(2));
                // Adding patientExercise to list
                patientExerciseList.add(patientExercise);
            } while (cursor.moveToNext());
        }

        // return patientExercise list
        return patientExerciseList;
    }

    public List<ExercisePhoto> getAllExercisePhotosOfPatient(int patientId) {
        List<ExercisePhoto> exercisesOfPatientList = new ArrayList<ExercisePhoto>();
        // Select All Query
        String selectQuery = "SELECT * FROM ExercisePhoto WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ExercisePhoto patientExercise = new ExercisePhoto();
                patientExercise.setId(Integer.parseInt(cursor.getString(0)));
                patientExercise.setPatientId(patientId);
                patientExercise.setPhotoBytes(cursor.getBlob(2));
                // Adding patientExercise to list
                exercisesOfPatientList.add(patientExercise);
            } while (cursor.moveToNext());
        }

        // return patientExercise list
        return exercisesOfPatientList;
    }

    public void deleteExercisePhoto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ExercisePhoto", "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int selectLastPhotoId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("ExercisePhoto", new String[]{"id",
                "patient_id", "photo"}, null, null, null, null, null);
        cursor.moveToLast();
        int lastId = Integer.parseInt(cursor.getString(0));
        return lastId;
    }


    //PatientImage table related functions

    public void addPatientImage(PatientImage patientImage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientImage.getPatientId());
        values.put("image_path", patientImage.getImagePath());

        // Inserting Row
        db.insert("PatientImage", null, values);

        // Closing database connection
        db.close();
    }

    public List<PatientImage> getAllPatientImages() {
        List<PatientImage> patientImageList = new ArrayList<PatientImage>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientImage ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientImage patientImage = new PatientImage();
                patientImage.setPatientId(Integer.parseInt(cursor.getString(0)));
                patientImage.setImagePath(cursor.getString(1));
                // Adding patientImage to list
                patientImageList.add(patientImage);
            } while (cursor.moveToNext());
        }

        // return patientImage list
        return patientImageList;
    }

    public List<String> getAllImagePathsOfPatient(int patientId) {
        List<String> allImagePathsOfPatient = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT image_path FROM PatientImage WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding patientImage to list
                allImagePathsOfPatient.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return patientImage list
        return allImagePathsOfPatient;
    }

    public void deletePatientImage(PatientImage patientImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PatientImage", "patient_id = ? AND image_path = ?",
                new String[]{String.valueOf(patientImage.getPatientId()), patientImage.getImagePath()});
        db.close();
    }


    //PatientVideo table related functions

    public void addPatientVideo(PatientVideo patientVideo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientVideo.getPatientId());
        values.put("video_path", patientVideo.getVideoPath());

        // Inserting Row
        db.insert("PatientVideo", null, values);

        // Closing database connection
        db.close();
    }

    public List<PatientVideo> getAllPatientVideos() {
        List<PatientVideo> patientVideoList = new ArrayList<PatientVideo>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientVideo ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientVideo patientVideo = new PatientVideo();
                patientVideo.setPatientId(Integer.parseInt(cursor.getString(0)));
                patientVideo.setVideoPath(cursor.getString(1));
                // Adding patientVideo to list
                patientVideoList.add(patientVideo);
            } while (cursor.moveToNext());
        }

        // return patientVideo list
        return patientVideoList;
    }

    public List<String> getAllVideoPathsOfPatient(int patientId) {
        List<String> allVideoPathsOfPatient = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT video_path FROM PatientVideo WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding patientVideo to list
                allVideoPathsOfPatient.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return patientVideo list
        return allVideoPathsOfPatient;
    }

    public void deletePatientVideo(PatientVideo patientVideo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PatientVideo", "patient_id = ? AND video_path = ?",
                new String[]{String.valueOf(patientVideo.getPatientId()), patientVideo.getVideoPath()});
        db.close();
    }


    //PatientDocument table related functions

    public void addPatientDocument(PatientDocument patientDocument) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientDocument.getPatientId());
        values.put("document_path", patientDocument.getDocumentPath());

        // Inserting Row
        db.insert("PatientDocument", null, values);

        // Closing database connection
        db.close();
    }

    public List<PatientDocument> getAllPatientDocuments() {
        List<PatientDocument> patientDocumentList = new ArrayList<PatientDocument>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientDocument ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientDocument patientDocument = new PatientDocument();
                patientDocument.setPatientId(Integer.parseInt(cursor.getString(0)));
                patientDocument.setDocumentPath(cursor.getString(1));
                // Adding patientDocument to list
                patientDocumentList.add(patientDocument);
            } while (cursor.moveToNext());
        }

        // return patientDocument list
        return patientDocumentList;
    }

    public List<String> getAllDocumentPathsOfPatient(int patientId) {
        List<String> allDocumentPathsOfPatient = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT document_path FROM PatientDocument WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding patientDocument to list
                allDocumentPathsOfPatient.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return patientDocument list
        return allDocumentPathsOfPatient;
    }

    public void deletePatientDocument(PatientDocument patientDocument) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PatientDocument", "patient_id = ? AND document_path = ?",
                new String[]{String.valueOf(patientDocument.getPatientId()), patientDocument.getDocumentPath()});
        db.close();
    }


    //WebsiteType table related functions

    public void addWebsiteType(WebsiteType websiteType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", websiteType.getName());
        values.put("url", websiteType.getUrl());
        values.put("description", websiteType.getDescription());
        // Inserting Row
        db.insert("WebsiteType", null, values);

        // Closing database connection
        db.close();
    }

    public List<WebsiteType> getAllWebsiteTypes() {
        List<WebsiteType> websiteTypeList = new ArrayList<WebsiteType>();
        // Select All Query
        String selectQuery = "SELECT * FROM WebsiteType ORDER BY name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WebsiteType websiteType = new WebsiteType();
                websiteType.setId(Integer.parseInt(cursor.getString(0)));
                websiteType.setName(cursor.getString(1));
                websiteType.setUrl(cursor.getString(2));
                websiteType.setDescription(cursor.getString(3));
                // Adding websiteType to list
                websiteTypeList.add(websiteType);
            } while (cursor.moveToNext());
        }

        // return websiteType list
        return websiteTypeList;
    }

    public WebsiteType getWebsiteType(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("WebsiteType", new String[]{"name",
                        "url", "description"}, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        WebsiteType websiteType = new WebsiteType(
                id,
                cursor.getString(0), cursor.getString(1), cursor.getString(2)); //name, url, description
        // return websiteType
        return websiteType;
    }

    public int updateWebsiteType(int id, WebsiteType websiteType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", websiteType.getName());
        values.put("url", websiteType.getUrl());
        values.put("description", websiteType.getDescription());

        // updating row
        return db.update("WebsiteType", values, "id = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteWebsiteType(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("WebsiteType", "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int selectLastWebsiteId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("WebsiteType", new String[]{"id"}, null, null, null, null, null);
        cursor.moveToLast();
        int lastId = Integer.parseInt(cursor.getString(0));
        return lastId;
    }


    //PatientWebsite table related functions

    public void addPatientWebsite(PatientWebsite patientWebsite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", patientWebsite.getPatientId());
        values.put("website_id", patientWebsite.getWebsiteTypeId());

        // Inserting Row
        db.insert("PatientWebsite", null, values);

        // Closing database connection
        db.close();
    }

    public List<PatientWebsite> getAllPatientWebsites() {
        List<PatientWebsite> patientWebsiteList = new ArrayList<PatientWebsite>();
        // Select All Query
        String selectQuery = "SELECT * FROM PatientWebsite ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PatientWebsite patientWebsite = new PatientWebsite();
                patientWebsite.setPatientId(Integer.parseInt(cursor.getString(0)));
                patientWebsite.setWebsiteTypeId(Integer.parseInt(cursor.getString(1)));
                // Adding patientWebsite to list
                patientWebsiteList.add(patientWebsite);
            } while (cursor.moveToNext());
        }

        // return patientWebsite list
        return patientWebsiteList;
    }

    public List<Integer> getAllWebsiteIdsOfPatient(int patientId) {
        List<Integer> allWebsiteIdsOfPatient = new ArrayList<Integer>();
        // Select All Query
        String selectQuery = "SELECT website_id FROM PatientWebsite WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding patientWebsite to list
                allWebsiteIdsOfPatient.add(Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());
        }

        // return patientWebsite list
        return allWebsiteIdsOfPatient;
    }

    public void deletePatientWebsite(PatientWebsite patientWebsite) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PatientWebsite", "patient_id = ? AND website_id = ?",
                new String[]{String.valueOf(patientWebsite.getPatientId()), String.valueOf(patientWebsite.getWebsiteTypeId())});
        db.close();
    }


    //Note table related functions

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", note.getPatientId());
        values.put("note_bytes", note.getNoteBytes());
        values.put("selected", note.isSelected());

        // Inserting Row
        db.insert("Note", null, values);

        // Closing database connection
        db.close();
    }

    public Note getNote(int noteId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Note", new String[]{"id",
                        "patient_id", "note_bytes", "selected"}, "id = ?",
                new String[]{String.valueOf(noteId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note();
        note.setId(Integer.parseInt(cursor.getString(0)));
        note.setPatientId(Integer.parseInt(cursor.getString(1)));
        note.setNoteBytes(cursor.getBlob(2));
        note.setSelected(cursor.getInt(3) > 0);
        // return note
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<Note>();
        // Select All Query
        String selectQuery = "SELECT * FROM Note ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setPatientId(Integer.parseInt(cursor.getString(1)));
                note.setNoteBytes(cursor.getBlob(2));
                note.setSelected(cursor.getInt(3) > 0);
                // Adding note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        // return note list
        return noteList;
    }

    public List<Note> getAllNotesOfPatient(int patientId) {
        List<Note> notesOfPatient = new ArrayList<Note>();
        // Select All Query
        String selectQuery = "SELECT * FROM Note WHERE patient_id = ? ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setPatientId(Integer.parseInt(cursor.getString(1)));
                note.setNoteBytes(cursor.getBlob(2));
                note.setSelected(cursor.getInt(3) > 0);
                // Adding note to list
                notesOfPatient.add(note);
            } while (cursor.moveToNext());
        }

        // return note list
        return notesOfPatient;
    }

    public List<Note> getSelectedNotesOfPatient(int patientId) {
        List<Note> notesOfPatient = new ArrayList<Note>();
        // Select All Query
        String selectQuery = "SELECT * FROM Note WHERE selected AND patient_id = ? ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setPatientId(Integer.parseInt(cursor.getString(1)));
                note.setNoteBytes(cursor.getBlob(2));
                note.setSelected(cursor.getInt(3) > 0);
                // Adding note to list
                notesOfPatient.add(note);
            } while (cursor.moveToNext());
        }

        // return note list
        return notesOfPatient;
    }

    public int updateNote(int noteId, Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("selected", note.isSelected());

        // updating row
        return db.update("Note", values, "id = ?",
                new String[]{String.valueOf(noteId)});
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Note", "id = ?",
                new String[]{String.valueOf(noteId)});
        db.close();
    }

    public int selectLastNoteId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Note", new String[]{"id"}, null, null, null, null, null);
        cursor.moveToLast();
        int lastId = Integer.parseInt(cursor.getString(0));
        return lastId;
    }


    //PainBeginning table related functions

    public void addPainBeginning(PainBeginning painBeginning) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", painBeginning.getPatient_id());
        values.put("intensity", painBeginning.getIntensity());
        values.put("location_teeth", painBeginning.getLocation_teeth());
        values.put("location_face_left", painBeginning.getLocation_face_left());
        values.put("location_face_right", painBeginning.getLocation_face_right());
        values.put("pain_pattern", painBeginning.getPain_pattern());
        values.put("dull", painBeginning.isDull());
        values.put("pulling", painBeginning.isPulling());
        values.put("stinging", painBeginning.isStinging());
        values.put("pulsating", painBeginning.isPulsating());
        values.put("burning", painBeginning.isBurning());
        values.put("pinsneedles", painBeginning.isPinsneedles());
        values.put("tingling", painBeginning.isTingling());
        values.put("numb", painBeginning.isNumb());
        values.put("comment", painBeginning.getComment());

        // Inserting Row
        db.insert("PainBeginning", null, values);

        // Closing database connection
        db.close();
    }

    public PainBeginning getPainBeginningOfPatient(int patient_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("PainBeginning", null, "patient_id = ?",
                new String[]{String.valueOf(patient_id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PainBeginning painBeginning = new PainBeginning();
        painBeginning.setPatient_id(Integer.parseInt(cursor.getString(0)));
        painBeginning.setIntensity(Integer.parseInt(cursor.getString(1)));
        painBeginning.setLocation_teeth(cursor.getBlob(2));
        painBeginning.setLocation_face_left(cursor.getBlob(3));
        painBeginning.setLocation_face_right(cursor.getBlob(4));
        painBeginning.setPain_pattern(cursor.getString(5));
        painBeginning.setDull(cursor.getInt(6) > 0);
        painBeginning.setPulling(cursor.getInt(7) > 0);
        painBeginning.setStinging(cursor.getInt(8) > 0);
        painBeginning.setPulsating(cursor.getInt(9) > 0);
        painBeginning.setBurning(cursor.getInt(10) > 0);
        painBeginning.setPinsneedles(cursor.getInt(11) > 0);
        painBeginning.setTingling(cursor.getInt(12) > 0);
        painBeginning.setNumb(cursor.getInt(13) > 0);
        painBeginning.setComment(cursor.getString(14));
        // return painBeginning
        return painBeginning;
    }

    public List<PainBeginning> getAllPainBeginnings() {
        List<PainBeginning> painBeginningList = new ArrayList<PainBeginning>();
        // Select All Query
        String selectQuery = "SELECT * FROM PainBeginning ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PainBeginning painBeginning = new PainBeginning();
                painBeginning.setPatient_id(Integer.parseInt(cursor.getString(0)));
                painBeginning.setIntensity(Integer.parseInt(cursor.getString(1)));
                painBeginning.setLocation_teeth(cursor.getBlob(2));
                painBeginning.setLocation_face_left(cursor.getBlob(3));
                painBeginning.setLocation_face_right(cursor.getBlob(4));
                painBeginning.setPain_pattern(cursor.getString(5));
                painBeginning.setDull(cursor.getInt(6) > 0);
                painBeginning.setPulling(cursor.getInt(7) > 0);
                painBeginning.setStinging(cursor.getInt(8) > 0);
                painBeginning.setPulsating(cursor.getInt(9) > 0);
                painBeginning.setBurning(cursor.getInt(10) > 0);
                painBeginning.setPinsneedles(cursor.getInt(11) > 0);
                painBeginning.setTingling(cursor.getInt(12) > 0);
                painBeginning.setNumb(cursor.getInt(13) > 0);
                painBeginning.setComment(cursor.getString(14));
                // Adding painBeginning to list
                painBeginningList.add(painBeginning);
            } while (cursor.moveToNext());
        }

        // return painBeginning list
        return painBeginningList;
    }

    public int updatePainBeginning(int patient_id, PainBeginning painBeginning) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("intensity", painBeginning.getIntensity());
        values.put("location_teeth", painBeginning.getLocation_teeth());
        values.put("location_face_left", painBeginning.getLocation_face_left());
        values.put("location_face_right", painBeginning.getLocation_face_right());
        values.put("pain_pattern", painBeginning.getPain_pattern());
        values.put("dull", painBeginning.isDull());
        values.put("pulling", painBeginning.isPulling());
        values.put("stinging", painBeginning.isStinging());
        values.put("pulsating", painBeginning.isPulsating());
        values.put("burning", painBeginning.isBurning());
        values.put("pinsneedles", painBeginning.isPinsneedles());
        values.put("tingling", painBeginning.isTingling());
        values.put("numb", painBeginning.isNumb());
        values.put("comment", painBeginning.getComment());

        // updating row
        return db.update("PainBeginning", values, "patient_id = ?",
                new String[]{String.valueOf(patient_id)});
    }

    public void deletePainBeginningOfPatient(int patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PainBeginning", "patient_id = ?",
                new String[]{String.valueOf(patient_id)});
        db.close();
    }

    public boolean existsPainBeginning(int patientId) {
        String selectQuery = "SELECT COUNT(*) FROM PainBeginning WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        cursor.moveToFirst();
        Integer count = Integer.parseInt(cursor.getString(0));

        return count != null && count > 0;
    }


    //PainCurrent table related functions

    public void addPainCurrent(PainCurrent painCurrent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", painCurrent.getPatient_id());
        values.put("intensity", painCurrent.getIntensity());
        values.put("location_teeth", painCurrent.getLocation_teeth());
        values.put("location_face_left", painCurrent.getLocation_face_left());
        values.put("location_face_right", painCurrent.getLocation_face_right());
        values.put("pain_pattern", painCurrent.getPain_pattern());
        values.put("dull", painCurrent.isDull());
        values.put("pulling", painCurrent.isPulling());
        values.put("stinging", painCurrent.isStinging());
        values.put("pulsating", painCurrent.isPulsating());
        values.put("burning", painCurrent.isBurning());
        values.put("pinsneedles", painCurrent.isPinsneedles());
        values.put("tingling", painCurrent.isTingling());
        values.put("numb", painCurrent.isNumb());
        values.put("comment", painCurrent.getComment());

        // Inserting Row
        db.insert("PainCurrent", null, values);

        // Closing database connection
        db.close();
    }

    public PainCurrent getPainCurrentOfPatient(int patient_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("PainCurrent", null, "patient_id = ?",
                new String[]{String.valueOf(patient_id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PainCurrent painCurrent = new PainCurrent();
        painCurrent.setPatient_id(Integer.parseInt(cursor.getString(0)));
        painCurrent.setIntensity(Integer.parseInt(cursor.getString(1)));
        painCurrent.setLocation_teeth(cursor.getBlob(2));
        painCurrent.setLocation_face_left(cursor.getBlob(3));
        painCurrent.setLocation_face_right(cursor.getBlob(4));
        painCurrent.setPain_pattern(cursor.getString(5));
        painCurrent.setDull(cursor.getInt(6) > 0);
        painCurrent.setPulling(cursor.getInt(7) > 0);
        painCurrent.setStinging(cursor.getInt(8) > 0);
        painCurrent.setPulsating(cursor.getInt(9) > 0);
        painCurrent.setBurning(cursor.getInt(10) > 0);
        painCurrent.setPinsneedles(cursor.getInt(11) > 0);
        painCurrent.setTingling(cursor.getInt(12) > 0);
        painCurrent.setNumb(cursor.getInt(13) > 0);
        painCurrent.setComment(cursor.getString(14));
        // return painCurrent
        return painCurrent;
    }

    public List<PainCurrent> getAllPainCurrents() {
        List<PainCurrent> painCurrentList = new ArrayList<PainCurrent>();
        // Select All Query
        String selectQuery = "SELECT * FROM PainCurrent ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PainCurrent painCurrent = new PainCurrent();
                painCurrent.setPatient_id(Integer.parseInt(cursor.getString(0)));
                painCurrent.setIntensity(Integer.parseInt(cursor.getString(1)));
                painCurrent.setLocation_teeth(cursor.getBlob(2));
                painCurrent.setLocation_face_left(cursor.getBlob(3));
                painCurrent.setLocation_face_right(cursor.getBlob(4));
                painCurrent.setPain_pattern(cursor.getString(5));
                painCurrent.setDull(cursor.getInt(6) > 0);
                painCurrent.setPulling(cursor.getInt(7) > 0);
                painCurrent.setStinging(cursor.getInt(8) > 0);
                painCurrent.setPulsating(cursor.getInt(9) > 0);
                painCurrent.setBurning(cursor.getInt(10) > 0);
                painCurrent.setPinsneedles(cursor.getInt(11) > 0);
                painCurrent.setTingling(cursor.getInt(12) > 0);
                painCurrent.setNumb(cursor.getInt(13) > 0);
                painCurrent.setComment(cursor.getString(14));
                // Adding painCurrent to list
                painCurrentList.add(painCurrent);
            } while (cursor.moveToNext());
        }

        // return painCurrent list
        return painCurrentList;
    }

    public int updatePainCurrent(int patient_id, PainCurrent painCurrent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("intensity", painCurrent.getIntensity());
        values.put("location_teeth", painCurrent.getLocation_teeth());
        values.put("location_face_left", painCurrent.getLocation_face_left());
        values.put("location_face_right", painCurrent.getLocation_face_right());
        values.put("pain_pattern", painCurrent.getPain_pattern());
        values.put("dull", painCurrent.isDull());
        values.put("pulling", painCurrent.isPulling());
        values.put("stinging", painCurrent.isStinging());
        values.put("pulsating", painCurrent.isPulsating());
        values.put("burning", painCurrent.isBurning());
        values.put("pinsneedles", painCurrent.isPinsneedles());
        values.put("tingling", painCurrent.isTingling());
        values.put("numb", painCurrent.isNumb());
        values.put("comment", painCurrent.getComment());

        // updating row
        return db.update("PainCurrent", values, "patient_id = ?",
                new String[]{String.valueOf(patient_id)});
    }

    public void deletePainCurrentOfPatient(int patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PainCurrent", "patient_id = ?",
                new String[]{String.valueOf(patient_id)});
        db.close();
    }

    public boolean existsPainCurrent(int patientId) {
        String selectQuery = "SELECT COUNT(*) FROM PainCurrent WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        cursor.moveToFirst();
        Integer count = Integer.parseInt(cursor.getString(0));

        return count != null && count > 0;
    }


    //PsychoSocialBefore table related functions

    public void addPsychoSocialBefore(PsychoSocialBefore psychoSocialBefore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", psychoSocialBefore.getPatient_id());

        values.put("pain_xpos", psychoSocialBefore.getPain_xpos());
        values.put("pain_ypos", psychoSocialBefore.getPain_ypos());
        values.put("pain_color", psychoSocialBefore.getPain_color());
        values.put("pain_size", psychoSocialBefore.getPain_size());

        values.put("family_xpos", psychoSocialBefore.getFamily_xpos());
        values.put("family_ypos", psychoSocialBefore.getFamily_ypos());
        values.put("family_color", psychoSocialBefore.getFamily_color());
        values.put("family_size", psychoSocialBefore.getFamily_size());

        values.put("work_xpos", psychoSocialBefore.getWork_xpos());
        values.put("work_ypos", psychoSocialBefore.getWork_ypos());
        values.put("work_color", psychoSocialBefore.getWork_color());
        values.put("work_size", psychoSocialBefore.getWork_size());

        values.put("finance_xpos", psychoSocialBefore.getFinance_xpos());
        values.put("finance_ypos", psychoSocialBefore.getFinance_ypos());
        values.put("finance_color", psychoSocialBefore.getFinance_color());
        values.put("finance_size", psychoSocialBefore.getFinance_size());

        values.put("event_xpos", psychoSocialBefore.getEvent_xpos());
        values.put("event_ypos", psychoSocialBefore.getEvent_ypos());
        values.put("event_color", psychoSocialBefore.getEvent_color());
        values.put("event_size", psychoSocialBefore.getEvent_size());

        // Inserting Row
        db.insert("PsychoSocialBefore", null, values);

        // Closing database connection
        db.close();
    }

    public PsychoSocialBefore getPsychoSocialBefore(int patient_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("PsychoSocialBefore", null, "patient_id = ?",
                new String[]{String.valueOf(patient_id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PsychoSocialBefore psychoSocialBefore = new PsychoSocialBefore();
        psychoSocialBefore.setPatient_id(Integer.parseInt(cursor.getString(0)));
        psychoSocialBefore.setPain_xpos(Integer.parseInt(cursor.getString(1)));
        psychoSocialBefore.setPain_ypos(Integer.parseInt(cursor.getString(2)));
        psychoSocialBefore.setPain_color(Integer.parseInt(cursor.getString(3)));
        psychoSocialBefore.setPain_size(Integer.parseInt(cursor.getString(4)));
        psychoSocialBefore.setFamily_xpos(Integer.parseInt(cursor.getString(5)));
        psychoSocialBefore.setFamily_ypos(Integer.parseInt(cursor.getString(6)));
        psychoSocialBefore.setFamily_color(Integer.parseInt(cursor.getString(7)));
        psychoSocialBefore.setFamily_size(Integer.parseInt(cursor.getString(8)));
        psychoSocialBefore.setWork_xpos(Integer.parseInt(cursor.getString(9)));
        psychoSocialBefore.setWork_ypos(Integer.parseInt(cursor.getString(10)));
        psychoSocialBefore.setWork_color(Integer.parseInt(cursor.getString(11)));
        psychoSocialBefore.setWork_size(Integer.parseInt(cursor.getString(12)));
        psychoSocialBefore.setFinance_xpos(Integer.parseInt(cursor.getString(13)));
        psychoSocialBefore.setFinance_ypos(Integer.parseInt(cursor.getString(14)));
        psychoSocialBefore.setFinance_color(Integer.parseInt(cursor.getString(15)));
        psychoSocialBefore.setFinance_size(Integer.parseInt(cursor.getString(16)));
        psychoSocialBefore.setEvent_xpos(Integer.parseInt(cursor.getString(17)));
        psychoSocialBefore.setEvent_ypos(Integer.parseInt(cursor.getString(18)));
        psychoSocialBefore.setEvent_color(Integer.parseInt(cursor.getString(19)));
        psychoSocialBefore.setEvent_size(Integer.parseInt(cursor.getString(20)));

        // return psychoSocialBefore
        return psychoSocialBefore;
    }

    public List<PsychoSocialBefore> getAllPsychoSocialBefores() {
        List<PsychoSocialBefore> psychoSocialBeforeList = new ArrayList<PsychoSocialBefore>();
        // Select All Query
        String selectQuery = "SELECT * FROM PsychoSocialBefore ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PsychoSocialBefore psychoSocialBefore = new PsychoSocialBefore();
                psychoSocialBefore.setPatient_id(Integer.parseInt(cursor.getString(0)));
                psychoSocialBefore.setPain_xpos(Integer.parseInt(cursor.getString(1)));
                psychoSocialBefore.setPain_ypos(Integer.parseInt(cursor.getString(2)));
                psychoSocialBefore.setPain_color(Integer.parseInt(cursor.getString(3)));
                psychoSocialBefore.setPain_size(Integer.parseInt(cursor.getString(4)));
                psychoSocialBefore.setFamily_xpos(Integer.parseInt(cursor.getString(5)));
                psychoSocialBefore.setFamily_ypos(Integer.parseInt(cursor.getString(6)));
                psychoSocialBefore.setFamily_color(Integer.parseInt(cursor.getString(7)));
                psychoSocialBefore.setFamily_size(Integer.parseInt(cursor.getString(8)));
                psychoSocialBefore.setWork_xpos(Integer.parseInt(cursor.getString(9)));
                psychoSocialBefore.setWork_ypos(Integer.parseInt(cursor.getString(10)));
                psychoSocialBefore.setWork_color(Integer.parseInt(cursor.getString(11)));
                psychoSocialBefore.setWork_size(Integer.parseInt(cursor.getString(12)));
                psychoSocialBefore.setFinance_xpos(Integer.parseInt(cursor.getString(13)));
                psychoSocialBefore.setFinance_ypos(Integer.parseInt(cursor.getString(14)));
                psychoSocialBefore.setFinance_color(Integer.parseInt(cursor.getString(15)));
                psychoSocialBefore.setFinance_size(Integer.parseInt(cursor.getString(16)));
                psychoSocialBefore.setEvent_xpos(Integer.parseInt(cursor.getString(17)));
                psychoSocialBefore.setEvent_ypos(Integer.parseInt(cursor.getString(18)));
                psychoSocialBefore.setEvent_color(Integer.parseInt(cursor.getString(19)));
                psychoSocialBefore.setEvent_size(Integer.parseInt(cursor.getString(20)));
                // Adding psychoSocialBefore to list
                psychoSocialBeforeList.add(psychoSocialBefore);
            } while (cursor.moveToNext());
        }

        // return psychoSocialBefore list
        return psychoSocialBeforeList;
    }

    public int updatePsychoSocialBefore(int patientId, PsychoSocialBefore psychoSocialBefore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", psychoSocialBefore.getPatient_id());

        values.put("pain_xpos", psychoSocialBefore.getPain_xpos());
        values.put("pain_ypos", psychoSocialBefore.getPain_ypos());
        values.put("pain_color", psychoSocialBefore.getPain_color());
        values.put("pain_size", psychoSocialBefore.getPain_size());

        values.put("family_xpos", psychoSocialBefore.getFamily_xpos());
        values.put("family_ypos", psychoSocialBefore.getFamily_ypos());
        values.put("family_color", psychoSocialBefore.getFamily_color());
        values.put("family_size", psychoSocialBefore.getFamily_size());

        values.put("work_xpos", psychoSocialBefore.getWork_xpos());
        values.put("work_ypos", psychoSocialBefore.getWork_ypos());
        values.put("work_color", psychoSocialBefore.getWork_color());
        values.put("work_size", psychoSocialBefore.getWork_size());

        values.put("finance_xpos", psychoSocialBefore.getFinance_xpos());
        values.put("finance_ypos", psychoSocialBefore.getFinance_ypos());
        values.put("finance_color", psychoSocialBefore.getFinance_color());
        values.put("finance_size", psychoSocialBefore.getFinance_size());

        values.put("event_xpos", psychoSocialBefore.getEvent_xpos());
        values.put("event_ypos", psychoSocialBefore.getEvent_ypos());
        values.put("event_color", psychoSocialBefore.getEvent_color());
        values.put("event_size", psychoSocialBefore.getEvent_size());

        // updating row
        return db.update("PsychoSocialBefore", values, "patient_id = ?",
                new String[]{String.valueOf(patientId)});
    }

    public void deletePsychoSocialBefore(int patientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PsychoSocialBefore", "patient_id = ?",
                new String[]{String.valueOf(patientId)});
        db.close();
    }

    public boolean existsPsychoSocialBefore(int patientId) {
        String selectQuery = "SELECT COUNT(*) FROM PsychoSocialBefore WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        cursor.moveToFirst();
        Integer count = Integer.parseInt(cursor.getString(0));

        return count != null && count > 0;
    }


    /*PsychoSocialAfter table related functions*/

    public void addPsychoSocialAfter(PsychoSocialAfter psychoSocialAfter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", psychoSocialAfter.getPatient_id());

        values.put("pain_xpos", psychoSocialAfter.getPain_xpos());
        values.put("pain_ypos", psychoSocialAfter.getPain_ypos());
        values.put("pain_color", psychoSocialAfter.getPain_color());
        values.put("pain_size", psychoSocialAfter.getPain_size());

        values.put("family_xpos", psychoSocialAfter.getFamily_xpos());
        values.put("family_ypos", psychoSocialAfter.getFamily_ypos());
        values.put("family_color", psychoSocialAfter.getFamily_color());
        values.put("family_size", psychoSocialAfter.getFamily_size());

        values.put("work_xpos", psychoSocialAfter.getWork_xpos());
        values.put("work_ypos", psychoSocialAfter.getWork_ypos());
        values.put("work_color", psychoSocialAfter.getWork_color());
        values.put("work_size", psychoSocialAfter.getWork_size());

        values.put("finance_xpos", psychoSocialAfter.getFinance_xpos());
        values.put("finance_ypos", psychoSocialAfter.getFinance_ypos());
        values.put("finance_color", psychoSocialAfter.getFinance_color());
        values.put("finance_size", psychoSocialAfter.getFinance_size());

        values.put("event_xpos", psychoSocialAfter.getEvent_xpos());
        values.put("event_ypos", psychoSocialAfter.getEvent_ypos());
        values.put("event_color", psychoSocialAfter.getEvent_color());
        values.put("event_size", psychoSocialAfter.getEvent_size());

        // Inserting Row
        db.insert("PsychoSocialAfter", null, values);

        // Closing database connection
        db.close();
    }

    public PsychoSocialAfter getPsychoSocialAfter(int patient_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("PsychoSocialAfter", null, "patient_id = ?",
                new String[]{String.valueOf(patient_id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PsychoSocialAfter psychoSocialAfter = new PsychoSocialAfter();
        psychoSocialAfter.setPatient_id(Integer.parseInt(cursor.getString(0)));
        psychoSocialAfter.setPain_xpos(Integer.parseInt(cursor.getString(1)));
        psychoSocialAfter.setPain_ypos(Integer.parseInt(cursor.getString(2)));
        psychoSocialAfter.setPain_color(Integer.parseInt(cursor.getString(3)));
        psychoSocialAfter.setPain_size(Integer.parseInt(cursor.getString(4)));
        psychoSocialAfter.setFamily_xpos(Integer.parseInt(cursor.getString(5)));
        psychoSocialAfter.setFamily_ypos(Integer.parseInt(cursor.getString(6)));
        psychoSocialAfter.setFamily_color(Integer.parseInt(cursor.getString(7)));
        psychoSocialAfter.setFamily_size(Integer.parseInt(cursor.getString(8)));
        psychoSocialAfter.setWork_xpos(Integer.parseInt(cursor.getString(9)));
        psychoSocialAfter.setWork_ypos(Integer.parseInt(cursor.getString(10)));
        psychoSocialAfter.setWork_color(Integer.parseInt(cursor.getString(11)));
        psychoSocialAfter.setWork_size(Integer.parseInt(cursor.getString(12)));
        psychoSocialAfter.setFinance_xpos(Integer.parseInt(cursor.getString(13)));
        psychoSocialAfter.setFinance_ypos(Integer.parseInt(cursor.getString(14)));
        psychoSocialAfter.setFinance_color(Integer.parseInt(cursor.getString(15)));
        psychoSocialAfter.setFinance_size(Integer.parseInt(cursor.getString(16)));
        psychoSocialAfter.setEvent_xpos(Integer.parseInt(cursor.getString(17)));
        psychoSocialAfter.setEvent_ypos(Integer.parseInt(cursor.getString(18)));
        psychoSocialAfter.setEvent_color(Integer.parseInt(cursor.getString(19)));
        psychoSocialAfter.setEvent_size(Integer.parseInt(cursor.getString(20)));

        // return psychoSocialAfter
        return psychoSocialAfter;
    }

    public List<PsychoSocialAfter> getAllPsychoSocialAfters() {
        List<PsychoSocialAfter> psychoSocialAfterList = new ArrayList<PsychoSocialAfter>();
        // Select All Query
        String selectQuery = "SELECT * FROM PsychoSocialAfter ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PsychoSocialAfter psychoSocialAfter = new PsychoSocialAfter();
                psychoSocialAfter.setPatient_id(Integer.parseInt(cursor.getString(0)));
                psychoSocialAfter.setPain_xpos(Integer.parseInt(cursor.getString(1)));
                psychoSocialAfter.setPain_ypos(Integer.parseInt(cursor.getString(2)));
                psychoSocialAfter.setPain_color(Integer.parseInt(cursor.getString(3)));
                psychoSocialAfter.setPain_size(Integer.parseInt(cursor.getString(4)));
                psychoSocialAfter.setFamily_xpos(Integer.parseInt(cursor.getString(5)));
                psychoSocialAfter.setFamily_ypos(Integer.parseInt(cursor.getString(6)));
                psychoSocialAfter.setFamily_color(Integer.parseInt(cursor.getString(7)));
                psychoSocialAfter.setFamily_size(Integer.parseInt(cursor.getString(8)));
                psychoSocialAfter.setWork_xpos(Integer.parseInt(cursor.getString(9)));
                psychoSocialAfter.setWork_ypos(Integer.parseInt(cursor.getString(10)));
                psychoSocialAfter.setWork_color(Integer.parseInt(cursor.getString(11)));
                psychoSocialAfter.setWork_size(Integer.parseInt(cursor.getString(12)));
                psychoSocialAfter.setFinance_xpos(Integer.parseInt(cursor.getString(13)));
                psychoSocialAfter.setFinance_ypos(Integer.parseInt(cursor.getString(14)));
                psychoSocialAfter.setFinance_color(Integer.parseInt(cursor.getString(15)));
                psychoSocialAfter.setFinance_size(Integer.parseInt(cursor.getString(16)));
                psychoSocialAfter.setEvent_xpos(Integer.parseInt(cursor.getString(17)));
                psychoSocialAfter.setEvent_ypos(Integer.parseInt(cursor.getString(18)));
                psychoSocialAfter.setEvent_color(Integer.parseInt(cursor.getString(19)));
                psychoSocialAfter.setEvent_size(Integer.parseInt(cursor.getString(20)));
                // Adding psychoSocialAfter to list
                psychoSocialAfterList.add(psychoSocialAfter);
            } while (cursor.moveToNext());
        }

        // return psychoSocialAfter list
        return psychoSocialAfterList;
    }

    public int updatePsychoSocialAfter(int patientId, PsychoSocialAfter psychoSocialAfter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", psychoSocialAfter.getPatient_id());

        values.put("pain_xpos", psychoSocialAfter.getPain_xpos());
        values.put("pain_ypos", psychoSocialAfter.getPain_ypos());
        values.put("pain_color", psychoSocialAfter.getPain_color());
        values.put("pain_size", psychoSocialAfter.getPain_size());

        values.put("family_xpos", psychoSocialAfter.getFamily_xpos());
        values.put("family_ypos", psychoSocialAfter.getFamily_ypos());
        values.put("family_color", psychoSocialAfter.getFamily_color());
        values.put("family_size", psychoSocialAfter.getFamily_size());

        values.put("work_xpos", psychoSocialAfter.getWork_xpos());
        values.put("work_ypos", psychoSocialAfter.getWork_ypos());
        values.put("work_color", psychoSocialAfter.getWork_color());
        values.put("work_size", psychoSocialAfter.getWork_size());

        values.put("finance_xpos", psychoSocialAfter.getFinance_xpos());
        values.put("finance_ypos", psychoSocialAfter.getFinance_ypos());
        values.put("finance_color", psychoSocialAfter.getFinance_color());
        values.put("finance_size", psychoSocialAfter.getFinance_size());

        values.put("event_xpos", psychoSocialAfter.getEvent_xpos());
        values.put("event_ypos", psychoSocialAfter.getEvent_ypos());
        values.put("event_color", psychoSocialAfter.getEvent_color());
        values.put("event_size", psychoSocialAfter.getEvent_size());

        // updating row
        return db.update("PsychoSocialAfter", values, "patient_id = ?",
                new String[]{String.valueOf(patientId)});
    }

    public void deletePsychoSocialAfter(int patientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PsychoSocialAfter", "patient_id = ?",
                new String[]{String.valueOf(patientId)});
        db.close();
    }

    public boolean existsPsychoSocialAfter(int patientId) {
        String selectQuery = "SELECT COUNT(*) FROM PsychoSocialAfter WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        cursor.moveToFirst();
        Integer count = Integer.parseInt(cursor.getString(0));

        return count != null && count > 0;
    }


    //ImprovementReason table related functions

    public void addImprovementReason(ImprovementReason improvementReason) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", improvementReason.getPatient_id());
        values.put("drugs", improvementReason.isDrugs());
        values.put("exercises", improvementReason.isExercises());
        values.put("awareness", improvementReason.isAwareness());
        values.put("other_reason", improvementReason.isOther_reason());
        values.put("other_reason_text", improvementReason.getOther_reason_text());

        // Inserting Row
        db.insert("ImprovementReason", null, values);

        // Closing database connection
        db.close();
    }

    public ImprovementReason getImprovementReason(int patient_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("ImprovementReason", null, "patient_id = ?",
                new String[]{String.valueOf(patient_id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ImprovementReason improvementReason = new ImprovementReason();
        improvementReason.setPatient_id(Integer.parseInt(cursor.getString(0)));
        improvementReason.setDrugs(cursor.getInt(1) > 0);
        improvementReason.setExercises(cursor.getInt(2) > 0);
        improvementReason.setAwareness(cursor.getInt(3) > 0);
        improvementReason.setOther_reason(cursor.getInt(4) > 0);
        improvementReason.setOther_reason_text(cursor.getString(5));

        // return improvementReason
        return improvementReason;
    }

    public List<ImprovementReason> getAllImprovementReasons() {
        List<ImprovementReason> improvementReasonList = new ArrayList<ImprovementReason>();
        // Select All Query
        String selectQuery = "SELECT * FROM ImprovementReason ORDER BY patient_id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ImprovementReason improvementReason = new ImprovementReason();
                improvementReason.setPatient_id(Integer.parseInt(cursor.getString(0)));
                improvementReason.setDrugs(cursor.getInt(1) > 0);
                improvementReason.setExercises(cursor.getInt(2) > 0);
                improvementReason.setAwareness(cursor.getInt(3) > 0);
                improvementReason.setOther_reason(cursor.getInt(4) > 0);
                improvementReason.setOther_reason_text(cursor.getString(5));

                // Adding improvementReason to list
                improvementReasonList.add(improvementReason);
            } while (cursor.moveToNext());
        }

        // return improvementReason list
        return improvementReasonList;
    }

    public int updateImprovementReason(int patientId, ImprovementReason improvementReason) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("patient_id", improvementReason.getPatient_id());
        values.put("drugs", improvementReason.isDrugs());
        values.put("exercises", improvementReason.isExercises());
        values.put("awareness", improvementReason.isAwareness());
        values.put("other_reason", improvementReason.isOther_reason());
        values.put("other_reason_text", improvementReason.getOther_reason_text());

        // updating row
        return db.update("ImprovementReason", values, "patient_id = ?",
                new String[]{String.valueOf(patientId)});
    }

    public void deleteImprovementReason(int patientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ImprovementReason", "patient_id = ?",
                new String[]{String.valueOf(patientId)});
        db.close();
    }

    public boolean existsImprovementReason(int patientId) {
        String selectQuery = "SELECT COUNT(*) FROM ImprovementReason WHERE patient_id = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});

        cursor.moveToFirst();
        Integer count = Integer.parseInt(cursor.getString(0));

        return count != null && count > 0;
    }

}