package itp341.truong.steven.a7;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewOrderActivity extends AppCompatActivity {

    CoffeeOrder co;

    TextView brew, size, sugar, milk, instructions;

    Button confirm, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        brew = (TextView) findViewById(R.id.brewTextView);

        size = (TextView) findViewById(R.id.sizeTextView);

        sugar = (TextView) findViewById(R.id.sugarTextView);

        milk = (TextView) findViewById(R.id.milkTextView);

        instructions = (TextView) findViewById(R.id.specialInstructionsTextView);

        confirm = (Button) findViewById(R.id.confirm);

        Intent i = getIntent();

        co = (CoffeeOrder) i.getSerializableExtra("COFFEE");

        brew.setText(co.getBrew());

        switch(co.getSize()) {
            case 0:
                size.setText("small");
                break;
            case 1:
                size.setText("medium");
                break;
            case 2:
                size.setText("large");
                break;
            default:
                break;
        }

        if (!co.isCream()) {
            milk.setText("no milk and ");
        }

        if(!co.isSugar()) {
            sugar.setText("no sugar");
        }

        instructions.setText(co.getInstructions());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });


        cancel = (Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, getIntent());
                finish();
            }
        });

    }
}
