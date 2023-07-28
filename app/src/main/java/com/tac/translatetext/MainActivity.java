package com.tac.translatetext;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;


    private EditText mSourcetext;
    private TextView mTranslatedText;
    private ImageButton btnSpeak,copyit;
    private String sourceText;
    String lang;
    Spinner  check,check2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  mSourceLang = findViewById(R.id.sourceLang);
        mSourcetext = findViewById(R.id.sourceText);
        mTranslatedText = findViewById(R.id.translatedText);
        btnSpeak = findViewById(R.id.anotherImageButton);
        copyit = findViewById(R.id.copy);
        check=findViewById(R.id.detectingSpinner);
        check2=findViewById(R.id.anotherSpinner);
        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {
            actionBar.hide();
        }
        copyit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTranslatedText.getText().toString().equals("Translating.."))
                {
                    Toast.makeText(MainActivity.this, "Please Wait to translate the language", Toast.LENGTH_SHORT).show();

                }
                else if(mTranslatedText.getText().toString().equals("") || mTranslatedText.getText().toString().equals("Translating Here.....")){
                    Toast.makeText(MainActivity.this, "Please translate the language", Toast.LENGTH_SHORT).show();

                }
                else{
                String textToCopy = mTranslatedText.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied_text", textToCopy);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }}
        });
        // Add TextWatcher to the EditText to detect text changes
        mSourcetext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Translation process is triggered whenever the text changes
                identifyLanguage();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changes
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check2.getSelectedItem().toString().equals("French")){lang="fr-FR";}
                else if(check2.getSelectedItem().toString().equals("Spanish")) {lang="es-ES";}

                else if(check2.getSelectedItem().toString().equals("German")) {lang="de-DE";}




                else if(check2.getSelectedItem().toString().equals("Chinese(Simp)")){lang="zh-CN";}




                else if(check2.getSelectedItem().toString().equals("Japanese")) {lang="ja-JP";}




                else if(check2.getSelectedItem().toString().equals("Korean")) {lang="ko-KR";}




                else if(check2.getSelectedItem().toString().equals("Arabic")) {lang="ar-SA";}



                else if(check2.getSelectedItem().toString().equals("Hindi")) {lang="hi-IN";}




                else if(check2.getSelectedItem().toString().equals("Russian")) {lang="ru-RU";}




                else if(check2.getSelectedItem().toString().equals("Portuguese")){lang="pt-PT";}



                else if(check2.getSelectedItem().toString().equals("Italian")) {lang="it-IT";}




                else if(check2.getSelectedItem().toString().equals("Dutch")) {lang="nl-NL";}



                else if(check2.getSelectedItem().toString().equals("Turkish")) {lang="tr-TR";}



                else if(check2.getSelectedItem().toString().equals("Thai")) {lang="th-TH";}



                else if(check2.getSelectedItem().toString().equals("Vietnamese")){lang="vi-VN";}



                else if(check2.getSelectedItem().toString().equals("Indonesian")) {lang="id-ID";}



                else if(check2.getSelectedItem().toString().equals("Malay")) {lang="ms-MY";}

                else if(check2.getSelectedItem().toString().equals("Urdu")) {lang="ur-PK";}

                else if(check2.getSelectedItem().toString().equals("English")) {lang="en-US";}


                else {lang="en-US";}
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang);
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    mSourcetext.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Your Device Dosent support Speach To Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSourcetext.setText(text.get(0));
                }
                break;
        }
    }
    private void identifyLanguage() {
        sourceText = mSourcetext.getText().toString();

        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

        //mSourceLang.setText("Detecting..");
        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s.equals("und")){
                   // Toast.makeText(getApplicationContext(),"Language Not Identified",Toast.LENGTH_SHORT).show();

                }
                else {
                    getLanguageCode(s);
                }
            }
        });
    }

    private void getLanguageCode(String language) {
        int langCode;
        switch (language){
            case "hi":
                langCode = FirebaseTranslateLanguage.HI;
                //mSourceLang.setText("Hindi");
                break;
            case "ar":
                langCode = FirebaseTranslateLanguage.AR;
                //mSourceLang.setText("Arabic");

                break;
            case "ur":
                langCode = FirebaseTranslateLanguage.UR;
               // mSourceLang.setText("Urdu");

                break;
                default:
                    langCode = 0;
        }

        translateText(langCode);
    }

    private void translateText(int langCode) {
        mTranslatedText.setText("Translating..");
        if(check.getSelectedItem().toString().equals("French")){
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                //from language
                .setSourceLanguage(langCode)
                // to language
                .setTargetLanguage(FirebaseTranslateLanguage.FR)
                .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });}
        else if(check.getSelectedItem().toString().equals("Spanish")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.ES)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }

        else if(check.getSelectedItem().toString().equals("German")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.DE)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }




        else if(check.getSelectedItem().toString().equals("Chinese(Simp)")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.ZH)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }




        else if(check.getSelectedItem().toString().equals("Japanese")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.JA)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }




        else if(check.getSelectedItem().toString().equals("Korean")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.KO)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }




        else if(check.getSelectedItem().toString().equals("Arabic")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.AR)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }



        else if(check.getSelectedItem().toString().equals("Hindi")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.HI)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }




        else if(check.getSelectedItem().toString().equals("Russian")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.RU)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }




        else if(check.getSelectedItem().toString().equals("Portuguese")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.PT)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }



        else if(check.getSelectedItem().toString().equals("Italian")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.IT)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }




        else if(check.getSelectedItem().toString().equals("Dutch")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.NL)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }



        else if(check.getSelectedItem().toString().equals("Turkish")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.TR)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }



        else if(check.getSelectedItem().toString().equals("Thai")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.TH)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }



        else if(check.getSelectedItem().toString().equals("Vietnamese")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.VI)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }



        else if(check.getSelectedItem().toString().equals("Indonesian")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.ID)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }



        else if(check.getSelectedItem().toString().equals("Malay")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.MS)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }

        else if(check.getSelectedItem().toString().equals("Urdu")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.UR)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }
        else if(check.getSelectedItem().toString().equals("English")) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.EN)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }


        else {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(langCode)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.EN)
                    .build();
            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);
            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();


            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTranslatedText.setText(s);
                        }
                    });
                }
            });
        }

    }
}
