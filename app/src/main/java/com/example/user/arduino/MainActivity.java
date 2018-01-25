package com.example.user.arduino;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Model> productList;
    private Button btnChangeTime;
    private Button btnChangeTime2;
    private TimePicker timePicker1;
    private int inorout;
    private Button aDD;

    TextView TH, TM;

    ListView lview;
    listviewAdapter adapter;

    int hours;
    int min;
    int totalh;
    int totalm;
    Date d1 = null;
    Date d2 = null;

    private TextView Pasok;
    private TextView Uwi;

    private int hour;
    private int minute;

    static final int TIME_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setCurrentTimeOnView();
        addListenerOnButton();

        Pasok = (TextView) findViewById(R.id.pasok);
        Uwi = (TextView) findViewById(R.id.uwi);

        TH = (TextView) findViewById(R.id.totaloras);
        TM = (TextView) findViewById(R.id.totalminuto);

        productList = new ArrayList<Model>();
        lview = (ListView) findViewById(R.id.listview);
        adapter = new listviewAdapter(this, productList);
        lview.setAdapter(adapter);

        //populateList();


        adapter.notifyDataSetChanged();
    }

    public void addListenerOnButton() {

        btnChangeTime = (Button) findViewById(R.id.timein);
        btnChangeTime2 = (Button) findViewById(R.id.timeout);
        aDD = (Button) findViewById(R.id.add);

        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                inorout = 1;
                showDialog(TIME_DIALOG_ID);

            }

        });

        btnChangeTime2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                inorout = 0;
                showDialog(TIME_DIALOG_ID);

            }

        });

        aDD.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String dateStart = Pasok.getText().toString();
                String dateStop = Uwi.getText().toString();

//HH converts hour in 24 hours format (0-23), day calculation
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");


                try {
                    d1 = format.parse(dateStart);
                    d2 = format.parse(dateStop);

                    //in milliseconds
                    long difference = d2.getTime() - d1.getTime();
                    if (difference < 0) {
                        Date dateMax = format.parse("24:00");
                        Date dateMin = format.parse("00:00");
                        difference = (dateMax.getTime() - d1.getTime()) + (d2.getTime() - dateMin.getTime());
                    }
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                    min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);


                    populateList();
                    adapter.notifyDataSetChanged();


                    totalm = totalm + min;
                    if(totalm == 60){
                        totalh = totalh+1;
                        totalm = 0;
                    } else if(totalm > 60) {
                        totalh = totalh+1;
                        totalm = totalm - 60;
                    }
                    totalh = totalh + hours;

                    TH.setText("Total hour(s): "+totalh);
                    TM.setText("Total minute(s): "+totalm);

                   // Toast.makeText(MainActivity.this, "" + totalh + "Hour(s) " + totalm + "Minute(s)", Toast.LENGTH_LONG).show();
                   // Toast.makeText(MainActivity.this, "" + hours + "Hour(s) " + min + "Minute(s)", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // TODO: handle exception
                }


            }

        });

    }

        @Override
        protected Dialog onCreateDialog(int id) {
            switch (id) {
                case TIME_DIALOG_ID:
                    // set time picker as current time
                    return new TimePickerDialog(this,
                            timePickerListener, hour, minute,false);

            }
            return null;
        }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;


                    // set current time into textview

                    if(inorout == 1) {
                        Pasok.setText(new StringBuilder().append(pad(hour))
                                .append(":").append(pad(minute)));
                    } else if (inorout == 0) {
                        Uwi.setText(new StringBuilder().append(pad(hour))
                                .append(":").append(pad(minute)));


                    }





                }
            };


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

//populate listview
       /* lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String sno = ((TextView)view.findViewById(R.id.sNo)).getText().toString();
                String product = ((TextView)view.findViewById(R.id.product)).getText().toString();
                String category = ((TextView)view.findViewById(R.id.category)).getText().toString();
                String price = ((TextView)view.findViewById(R.id.price)).getText().toString();

                Toast.makeText(getApplicationContext(),
                        "S no : " + sno +"\n"
                                +"Product : " + product +"\n"
                                +"Category : " +category +"\n"
                                +"Price : " +price, Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public void populateList() {

        Model item1, item2, item3, item4, item5;

        item1 = new Model(""+Pasok.getText().toString(), ""+Uwi.getText().toString(), ""+hours, ""+min);
        productList.add(item1);



       /* item2 = new Model("2", "Orange (Sunkist navel)", "Fruits", "₹. 100");
        productList.add(item2);

        item3 = new Model("3", "Tomato", "Vegetable", "₹. 50");
        productList.add(item3);

        item4 = new Model("4", "Carrot", "Vegetable", "₹. 80");
        productList.add(item4);

        item5 = new Model("5", "Banana (Cavendish)", "Fruits", "₹. 100");
        productList.add(item5);*/
    }

    }
