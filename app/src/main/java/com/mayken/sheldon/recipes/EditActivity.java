package com.mayken.sheldon.recipes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;

public class EditActivity extends ActionBarActivity {

    public final static int IngredientsCapture = 1;
    public final static int StepsCapture = 2;
    public final static String RECIPE_NAME_KEY = "com.mayken.sheldon.recipes.newname";

    private LinearLayout ingredientView;
    private LinearLayout blankIngredientField;

    private LinearLayout stepsView;
    private LinearLayout blankStepField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(RECIPE_NAME_KEY));

        TabHost tabs = (TabHost)findViewById(R.id.edit_tab_host);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("ingredientsSpec");
        spec.setContent(R.id.tab_ingredients);
        spec.setIndicator("Ingredients");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("stepsSpec");
        spec.setContent(R.id.tab_steps);
        spec.setIndicator("Steps");
        tabs.addTab(spec);

        ingredientView = (LinearLayout)findViewById(R.id.edit_indgredient_list);
        stepsView = (LinearLayout)findViewById(R.id.edit_steps_list);

        newIngredientView("");
        newStepView("");

        Button captureButton = (Button)findViewById(R.id.edit_ingredients_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(IngredientsCapture);
            }
        });

        captureButton = (Button)findViewById(R.id.edit_steps_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(StepsCapture);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save || id == R.id.action_cancel) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newIngredientView(String ingredientDesc) {
        final LinearLayout currentIngredientView = new LinearLayout(this);
        currentIngredientView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        currentIngredientView.setOrientation(LinearLayout.HORIZONTAL);

        Button deleteButton = new Button(this);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        deleteButton.setText("X");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIngredientView != blankIngredientField) {
                    ingredientView.removeView(currentIngredientView);
                }
            }
        });
        currentIngredientView.addView(deleteButton);

        EditText newIngredientText = new EditText(this);
        newIngredientText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
        ));
        newIngredientText.setHint("Ingredient Here...");
        newIngredientText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (blankIngredientField == currentIngredientView) {
                    blankIngredientField = null;
                    EditActivity.this.newIngredientView("");
                } else if (s.length() == 0) {
                    ingredientView.removeView(blankIngredientField);
                    blankIngredientField = currentIngredientView;
                }
            }
        });
        currentIngredientView.addView(newIngredientText);

        ingredientView.addView(currentIngredientView);
        if (!ingredientDesc.isEmpty()) {
            newIngredientText.setText(ingredientDesc);
            blankIngredientField = currentIngredientView;
        }
    }

    private void newStepView(String stepDesc) {
        final LinearLayout currentStepView = new LinearLayout(this);
        currentStepView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        currentStepView.setOrientation(LinearLayout.HORIZONTAL);

        Button deleteButton = new Button(this);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        deleteButton.setText("X");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStepView != blankStepField) {
                    stepsView.removeView(currentStepView);
                }
            }
        });
        currentStepView.addView(deleteButton);

        EditText newStepText = new EditText(this);
        newStepText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
        ));
        newStepText.setHint("Step Here...");
        newStepText.setLines(4);
        newStepText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (blankStepField == currentStepView) {
                    blankStepField = null;
                    EditActivity.this.newStepView("");
                }
            }
        });
        currentStepView.addView(newStepText);

        stepsView.addView(currentStepView);

        if (!stepDesc.isEmpty()) {
            newStepText.setText(stepDesc);
            blankStepField = currentStepView;
        }
    }


    private void captureImage(int requestCode) {
        File imageDirectories = new File(Environment.getExternalStorageDirectory(), "tmp");
        imageDirectories.mkdirs();

        File imageFile = new File(imageDirectories.getPath(), "tmpimageshot.jpg");
        Uri outputFileUri = Uri.fromFile(imageFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            processImageForRequestCode(requestCode);
        }
    }

    private void processImageForRequestCode(int requestCode) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        File imgDir = new File(Environment.getExternalStorageDirectory(), "tmp");
        File imgFile = new File(imgDir.getPath(), "tmpimageshot.jpg");

        OcrTask task = new OcrTask();
        task.operationCode = requestCode;
        task.execute(imgFile.getPath());
    }


    private class OcrTask extends AsyncTask<String, Void, String> {

        public int operationCode;
        private ProgressDialog dialog;

        @Override protected void onPreExecute() {
            dialog = ProgressDialog.show(EditActivity.this, "", "Reading Image...", true);
            dialog.show();
        }

        @Override protected String doInBackground(String... params) {
            String imgPath = params[0];

            int rotate = 0;
            try {
                ExifInterface exif = new ExifInterface(imgPath);
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                switch (exifOrientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            if (rotate != 0) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                Matrix matrix = new Matrix();
                matrix.preRotate(rotate);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
            }
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            TessBaseAPI baseAPI = new TessBaseAPI();
            baseAPI.init("/sdcard/recipes/langtraindata/", "eng");
            baseAPI.setImage(bitmap);

            String imgText = baseAPI.getUTF8Text();
            baseAPI.end();


            return imgText;
        }

        @Override protected void onPostExecute(String result) {
            if (operationCode == IngredientsCapture) {
                // process multiple lines
            } else {
                EditActivity.this.newStepView(result);
            }

            dialog.dismiss();
        }
    }

}
