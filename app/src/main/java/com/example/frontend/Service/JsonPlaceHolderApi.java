package com.example.frontend.Service;

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
import com.example.frontend.Models.WebsiteType;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("patient")
    Call<List<Patient>> getAllPatients();

    @GET("patient/{id}")
    Call<Patient> getPatient(@Path("id") int patient_id);

    @GET("patient/patient_last_id")
    Call<Integer> getLastPatientId();

    @POST("patient")
    Call<ResponseBody> createPatient(@Body Patient patient);

    @PUT("patient/{id}")
    Call<ResponseBody> updatePatient(@Path("id") int patient_id, @Body Patient patient);

    @DELETE("patient/{id}")
    Call<ResponseBody> deletePatient(@Path("id") int patient_id);

    //Drug Page

    @POST("drugtype")
    Call<ResponseBody> createDrugType(@Body DrugType drugType);

    @PUT("drugtype/{id}")
    Call<ResponseBody> updateDrugType(@Path("id") int drugtype_id, @Body DrugType drugType);

    @GET("drugtype/{id}")
    Call<DrugType> getDrugType(@Path("id") int drugtype_id);

    @GET("drugtype")
    Call<List<DrugType>> getAllDrugTypes();

    @DELETE("drugtype/{id}")
    Call<ResponseBody> deleteDrugType(@Path("id") int drugtype_id);

    @GET("drugtype_last_id")
    Call<Integer> getLastDrugTypeId();

    @GET("patientdrug/{patient_id}")
    Call<List<PatientDrug>> getAllDrugsOfPatient(@Path("patient_id") int patient_id);

    @POST("patientdrug")
    Call<ResponseBody> createPatientDrug(@Body PatientDrug patientDrug);

    @DELETE("patientdrug/{patient_id}/{drugtype_id}")
    Call<ResponseBody> deletePatientDrug(@Path("patient_id") int patient_id, @Path("drugtype_id") int drugtype_id);


    //Diagnosis Page

    @POST("diagnosistype")
    Call<ResponseBody> createDiagnosisType(@Body DiagnosisType diagnosisType);

    @PUT("diagnosistype/{id}")
    Call<ResponseBody> updateDiagnosisType(@Path("id") int diagnosistype_id, @Body DiagnosisType diagnosisType);

    @GET("diagnosistype")
    Call<List<DiagnosisType>> getAllDiagnosisTypes();

    @GET("diagnosistype/{id}")
    Call<DiagnosisType> getDiagnosisType(@Path("id") int diagnosistype_id);

    @GET("diagnosistype/type")
    Call<List<String>> getDiagnosisClasses();

    @GET("diagnosistype/type={type}")
    Call<List<DiagnosisType>> getAllDiagnosisTypesOfClass(@Path("type") String type);

    @DELETE("diagnosistype/{id}")
    Call<ResponseBody> deleteDiagnosisType(@Path("id") int diagnosistype_id);

    @GET("diagnosistype_last_id")
    Call<Integer> getLastDiagnosisTypeId();

    @GET("patientdiagnosis/{patient_id}/type={type}")
    Call<List<PatientDiagnosis>> getPatientDiagnosesOfClass(@Path("patient_id") int patient_id, @Path("type") String type);

    @GET("patientdiagnosis/{patient_id}")
    Call<List<PatientDiagnosis>> getPatientDiagnoses(@Path("patient_id") int patient_id);

    @POST("patientdiagnosis")
    Call<ResponseBody> createPatientDiagnosis(@Body PatientDiagnosis patientDiagnosis);

    @DELETE("patientdiagnosis/{patient_id}/{diagnosistype_id}")
    Call<ResponseBody> deletePatientDiagnosis(@Path("patient_id") int patient_id, @Path("diagnosistype_id") int diagnosistype_id);


    //Note Page

    @GET("note/patient_id={patient_id}")
    Call<List<Note>> getAllNotesOfPatient(@Path("patient_id") int patient_id);

    @GET("note/selected/patient_id={patient_id}")
    Call<List<Note>> getSelectedNotesOfPatient(@Path("patient_id") int patient_id);

    @GET("note/{id}")
    Call<Note> getNote(@Path("id") int note_id);

    @GET("note_last_id")
    Call<Integer> getLastNoteId();

    @POST("note")
    Call<ResponseBody> createNote(@Body Note note);

    @DELETE("note/{id}")
    Call<ResponseBody> deleteNote(@Path("id") int note_id);

    @PUT("note/{id}")
    Call<ResponseBody> updateNote(@Path("id") int note_id, @Body Note note);


    //Exercise Page
    @GET("exercisetype")
    Call<List<ExerciseType>> getAllExerciseTypes();

    @GET("patientexercise/{id}")
    Call<List<PatientExercise>> getSelectedPatientExercises(@Path("id") int patient_id);

    @POST("patientexercise")
    Call<ResponseBody> createPatientExercise(@Body PatientExercise patientExercise);

    @DELETE("patientexercise/{patient_id}/{exercisetype_title}")
    Call<ResponseBody> deletePatientExercise(@Path("patient_id") int patient_id, @Path("exercisetype_title") String exercisetype_title);

    @POST("exercisephoto")
    Call<ResponseBody> createExercisePhoto(@Body ExercisePhoto exercisePhoto);

    @GET("exercisephoto_last_id")
    Call<Integer> getLastPhotoId();

    @GET("exercisephoto/{id}")
    Call<List<ExercisePhoto>> getExercisePhotosOfPatient(@Path("id") int patient_id);

    @DELETE("exercisephoto/{id}")
    Call<ResponseBody> deleteExercisePhoto(@Path("id") int photo_id);


    //PsychoSocial Page

    @POST("psychosocial/before")
    Call<ResponseBody> createPsychoSocialBefore(@Body PsychoSocialBefore psychoSocialBefore);

    @PUT("psychosocial/before/{patient_id}")
    Call<ResponseBody> updatePsychoSocialBefore(@Path("patient_id") int patient_id, @Body PsychoSocialBefore psychoSocialBefore);

    @GET("psychosocial/before/{patient_id}")
    Call<PsychoSocialBefore> getPsychoSocialBefore(@Path("patient_id") int patient_id);

    @GET("psychosocial/before/exists/{patient_id}")
    Call<Boolean> existsPsychoSocialBefore(@Path("patient_id") int patient_id);


    @POST("psychosocial/after")
    Call<ResponseBody> createPsychoSocialAfter(@Body PsychoSocialAfter psychoSocialAfter);

    @PUT("psychosocial/after/{patient_id}")
    Call<ResponseBody> updatePsychoSocialAfter(@Path("patient_id") int patient_id, @Body PsychoSocialAfter psychoSocialAfter);

    @GET("psychosocial/after/{patient_id}")
    Call<PsychoSocialAfter> getPsychoSocialAfter(@Path("patient_id") int patient_id);

    @GET("psychosocial/after/exists/{patient_id}")
    Call<Boolean> existsPsychoSocialAfter(@Path("patient_id") int patient_id);


    @POST("psychosocial/reason")
    Call<ResponseBody> createImprovementReason(@Body ImprovementReason improvementReason);

    @PUT("psychosocial/reason/{patient_id}")
    Call<ResponseBody> updateImprovementReason(@Path("patient_id") int patient_id, @Body ImprovementReason improvementReason);

    @GET("psychosocial/reason/{patient_id}")
    Call<ImprovementReason> getImprovementReason(@Path("patient_id") int patient_id);

    @GET("psychosocial/reason/exists/{patient_id}")
    Call<Boolean> existsImprovementReason(@Path("patient_id") int patient_id);



    //Media Page

    @POST("patientimage")
    Call<ResponseBody> createPatientImage(@Body PatientImage patientImage);

    @GET("patientimage")
    Call<List<PatientImage>> getAllPatientImages();

    @GET("patientimage/{patient_id}")
    Call<List<String>> getAllImagePathsOfPatient(@Path("patient_id") int patient_id);

    @HTTP(method = "DELETE", path = "patientimage", hasBody = true)
    Call<ResponseBody> deletePatientImage(@Body PatientImage patientImage);



    @POST("patientvideo")
    Call<ResponseBody> createPatientVideo(@Body PatientVideo patientVideo);

    @GET("patientvideo")
    Call<List<PatientVideo>> getAllPatientVideos();

    @GET("patientvideo/{patient_id}")
    Call<List<String>> getAllVideoPathsOfPatient(@Path("patient_id") int patient_id);

    @HTTP(method = "DELETE", path = "patientvideo", hasBody = true)
    Call<ResponseBody> deletePatientVideo(@Body PatientVideo patientVideo);



    @POST("patientdocument")
    Call<ResponseBody> createPatientDocument(@Body PatientDocument patientDocument);

    @GET("patientdocument")
    Call<List<PatientDocument>> getAllPatientDocuments();

    @GET("patientdocument/{patient_id}")
    Call<List<String>> getAllDocumentPathsOfPatient(@Path("patient_id") int patient_id);

    @HTTP(method = "DELETE", path = "patientdocument", hasBody = true)
    Call<ResponseBody> deletePatientDocument(@Body PatientDocument patientDocument);



    @POST("websitetype")
    Call<ResponseBody> createWebsiteType(@Body WebsiteType websiteType);

    @GET("websitetype")
    Call<List<WebsiteType>> getAllWebsiteTypes();

    @GET("websitetype/{id}")
    Call<WebsiteType> getWebsiteType(@Path("id") int id);

    @GET("websitetype_last_id")
    Call<Integer> getLastWebsiteTypeId();

    @DELETE("websitetype/{id}")
    Call<ResponseBody> deleteWebsiteType(@Path("id") int id);

    @PUT("websitetype/{id}")
    Call<ResponseBody> updateWebsiteType(@Path("id") int id, @Body WebsiteType websiteType);



    @POST("patientwebsite")
    Call<ResponseBody> createPatientWebsite(@Body PatientWebsite patientWebsite);

    @GET("patientwebsite")
    Call<List<PatientWebsite>> getAllPatientWebsites();

    @GET("patientwebsite/{patient_id}")
    Call<List<Integer>> getAllWebsiteIdsOfPatient(@Path("patient_id") int patient_id);

    @HTTP(method = "DELETE", path = "patientwebsite", hasBody = true)
    Call<ResponseBody> deletePatientWebsite(@Body PatientWebsite patientWebsite);


    //Pain Page
    @POST("pain/beginning")
    Call<ResponseBody> createPainBeginning(@Body PainBeginning painBeginning);

    @PUT("pain/beginning/{patient_id}")
    Call<ResponseBody> updatePainBeginning(@Path("patient_id") int patient_id, @Body PainBeginning painBeginning);

    @GET("pain/beginning/{patient_id}")
    Call<PainBeginning> getPainBeginning(@Path("patient_id") int patient_id);

    @GET("pain/beginning/exists/{patient_id}")
    Call<Boolean> existsPainBeginning(@Path("patient_id") int patient_id);


    @POST("pain/current")
    Call<ResponseBody> createPainCurrent(@Body PainCurrent painCurrent);

    @PUT("pain/current/{patient_id}")
    Call<ResponseBody> updatePainCurrent(@Path("patient_id") int patient_id, @Body PainCurrent painCurrent);

    @GET("pain/current/{patient_id}")
    Call<PainCurrent> getPainCurrent(@Path("patient_id") int patient_id);

    @GET("pain/current/exists/{patient_id}")
    Call<Boolean> existsPainCurrent(@Path("patient_id") int patient_id);
}
