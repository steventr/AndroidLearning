package itp341.truong.steven.a7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final String PREFERENCES = "FAVORITE_COFFEE";

    static final String FAVORITE_SIZE = "FAVORITE_SIZE";
    static final String FAVORITE_BREW = "FAVORITE_BREW";
    static final String FAVORITE_MILK = "FAVORITE_MILK";
    static final String FAVORITE_SUGAR = "FAVORITE_SUGAR";
    static final String FAVORITE_INSTRUCTIONS = "FAVORITE_INSTRUCTIONS";
    RadioGroup size;

    Spinner brew;

    CheckBox milk;

    Switch sugar;

    EditText instructions;

    Button load, save, order, clear;

    CoffeeOrder coffeeOrderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coffeeOrderModel = new CoffeeOrder();

        size = (RadioGroup) findViewById(R.id.sizeSelection);

        size.clearCheck();

        size.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.small:
                        coffeeOrderModel.setSize(0);
                        break;
                    case R.id.medium:
                        coffeeOrderModel.setSize(1);
                        break;
                    case R.id.large:
                        coffeeOrderModel.setSize(2);
                        break;
                    default:
                        break;
                }
            }
        });

        brew = (Spinner) findViewById(R.id.coffeeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.coffees, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        brew.setAdapter(adapter);

        brew.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                switch (position) {
                    case 0:
                        coffeeOrderModel.setBrew("Kona");
                        break;
                    case 1:
                        coffeeOrderModel.setBrew("Arabica");
                        break;
                    case 2:
                        coffeeOrderModel.setBrew("Turkish");
                        break;
                    case 3:
                        coffeeOrderModel.setBrew("Irish");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                coffeeOrderModel.setBrew("Plain");
            }

        });

        milk = (CheckBox) findViewById(R.id.milkCheckbox);

        milk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                coffeeOrderModel.setCream(isChecked);
            }
        });

        sugar = (Switch) findViewById(R.id.sugarSwitch);

        sugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                coffeeOrderModel.setSugar(isChecked);
            }
        });

        load = (Button) findViewById(R.id.loadButton);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);

                if (prefs.contains(MainActivity.FAVORITE_BREW)) {
                    String savedBrew = prefs.getString(MainActivity.FAVORITE_BREW, "Kona");
                    coffeeOrderModel.setBrew(savedBrew);
                    switch (savedBrew) {
                        case "Kona":
                            brew.setSelection(0);
                            break;
                        case "Arabica":
                            brew.setSelection(1);
                            break;
                        case "Turkish":
                            brew.setSelection(2);
                            break;
                        case "Irish":
                            brew.setSelection(3);
                            break;
                        default:
                            break;
                    }
                }

                if (prefs.contains(MainActivity.FAVORITE_MILK)) {
                    Boolean savedMilk = prefs.getBoolean(MainActivity.FAVORITE_MILK, false);
                    coffeeOrderModel.setCream(savedMilk);
                    milk.setChecked(savedMilk);
                }

                if (prefs.contains(MainActivity.FAVORITE_SUGAR)) {
                    Boolean savedSugar = prefs.getBoolean(MainActivity.FAVORITE_SUGAR, false);
                    coffeeOrderModel.setSugar(savedSugar);
                    sugar.setChecked(savedSugar);
                }

                if (prefs.contains(MainActivity.FAVORITE_INSTRUCTIONS)) {
                    String savedInstructions = prefs.getString(MainActivity.FAVORITE_INSTRUCTIONS, "");
                    coffeeOrderModel.setInstructions(savedInstructions);
                    instructions.setText(savedInstructions);

                }

                if (prefs.contains(MainActivity.FAVORITE_SIZE)) {
                    int savedSize = prefs.getInt(MainActivity.FAVORITE_SIZE, 0);
                    coffeeOrderModel.setSize(savedSize);
                    switch (savedSize) {
                        case 0:
                            size.check(R.id.small);
                            break;
                        case 1:
                            size.check(R.id.medium);
                            break;
                        case 2:
                            size.check(R.id.large);
                            break;
                        default:
                            break;
                    }

                }

                Toast.makeText(getApplicationContext(), "Successfully loaded!", Toast.LENGTH_SHORT).show();
            }

        });

        save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();

                prefsEditor.putString(MainActivity.FAVORITE_BREW, coffeeOrderModel.getBrew());
                prefsEditor.putString(MainActivity.FAVORITE_INSTRUCTIONS, coffeeOrderModel.getInstructions());
                prefsEditor.putInt(MainActivity.FAVORITE_SIZE, coffeeOrderModel.getSize());
                prefsEditor.putBoolean(MainActivity.FAVORITE_MILK, coffeeOrderModel.isCream());
                prefsEditor.putBoolean(MainActivity.FAVORITE_SUGAR, coffeeOrderModel.isSugar());

                prefsEditor.commit();

                Toast.makeText(getApplicationContext(), "Successfully saved favorite coffeee", Toast.LENGTH_SHORT).show();
            }
        });


        instructions = (EditText) findViewById(R.id.instructionsEditText);

        instructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                coffeeOrderModel.setInstructions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        order = (Button) findViewById(R.id.orderButton);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size.getCheckedRadioButtonId() != -1 ) {
                    Intent i = new Intent(getApplicationContext(), ViewOrderActivity.class);
                    i.putExtra("COFFEE", coffeeOrderModel);
                    startActivityForResult(i, 0);
                }
                else {
                    Toast.makeText(getApplicationContext(), "You have to make sure you picked a size!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear = (Button) findViewById(R.id.clearButton);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                size.clearCheck();

                brew.setSelection(0);

                instructions.clearComposingText();

                sugar.setChecked(false);
                milk.setChecked(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            size.clearCheck();

            brew.setSelection(0);

            instructions.setText("");

            sugar.setChecked(false);
            milk.setChecked(false);
        }
        else
        {
            CoffeeOrder coffeeOrderData = (CoffeeOrder) data.getSerializableExtra("COFFEE");
            switch(coffeeOrderData.getSize())
            {
                case 0:
                    size.check(R.id.small);
                    break;
                case 1:
                    size.check(R.id.medium);
                    break;
                case 2:
                    size.check(R.id.large);
                    break;
                default:
                    break;
            }

            if (coffeeOrderData.getBrew().equals("Kona"))
            {
                brew.setSelection(0);
            }
            else if (coffeeOrderData.getBrew().equals("Arabica"))
            {
                brew.setSelection(1);
            }
            else if (coffeeOrderData.getBrew().equals("Turkish"))
            {
                brew.setSelection(2);
            }
            else {
                brew.setSelection(3);
            }


            instructions.setText(coffeeOrderData.getInstructions());

            sugar.setChecked(coffeeOrderData.isSugar());
            milk.setChecked(coffeeOrderData.isCream());

            Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}
