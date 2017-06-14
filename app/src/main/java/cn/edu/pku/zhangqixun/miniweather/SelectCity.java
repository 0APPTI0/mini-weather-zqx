package cn.edu.pku.zhangqixun.miniweather;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.zhangqixun.R;
import cn.edu.pku.zhangqixun.app.MyApplication;
import cn.edu.pku.zhangqixun.bean.City;

/**
 * Created by zhangqixun on 16/7/6.
 */
public class SelectCity extends Activity implements View.OnClickListener{

    private ImageView mBackBtn;
    private MyApplication mApplication;
    private ListView lv;
    private List<String> mStringList;
    private EditText edit_search;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        mApplication = (MyApplication) getApplication();
        lv = (ListView) findViewById(R.id.lv);
        mStringList = mApplication.getCityList();

        adapter = new ArrayAdapter<String>(SelectCity.this, android.R.layout.simple_expandable_list_item_1,mStringList
        );

        lv.setAdapter(adapter);

        //单击listView中的view
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("selectcityname",mStringList.get(i));
                Toast.makeText(SelectCity.this,"你单击了："+i,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectCity.this,MainActivity.class);//这里MainActivity.class是主活动。在主活动中再通过所选择的城市查询天气
                intent.putExtra("cityName",mStringList.get(i));
//                startActivity(intent);

                setResult(RESULT_OK,intent);
                finish();
            }
        });

        edit_search = (EditText) findViewById(R.id.search_edit);
        edit_search.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                Log.d("beforeTextChanged",s.toString());
//                temp =s;
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//根据输入模糊查询
//                Log.d("onTextChanged",s.toString());
//                List<City> mCityList=((MyApplication) MyApplication.getInstance()).getmCityDB().getAllCityByName(s.toString());  //查询获取当前条件下的城市
//
//                runOnUiThread(new Runnable()
//                {
//                    public void run()
//                    {
//                        adapter = new ArrayAdapter<String>(SelectCity.this, android.R.layout.simple_expandable_list_item_1,mStringList
//                        );
//
//                        lv.setAdapter(adapter);
//                        Log.d("onTextChanged","UIThread");
//                        adapter.notifyDataSetChanged();
//                    }
//
//                });

            }
            @Override
            public void afterTextChanged(Editable s){

                Log.d("onTextChanged",s.toString());

                mStringList.clear();
                List<City> mCityList=((MyApplication) MyApplication.getInstance()).getmCityDB().getAllCityByName(s.toString());  //查询获取当前条件下的城市



                for (City city : mCityList) {

                    String cityName = city.getCity();
                    mStringList.add(cityName);
                }

//                adapter = new ArrayAdapter<String>(SelectCity.this, android.R.layout.simple_expandable_list_item_1,mStringList
//                );

                lv.setAdapter(adapter);


                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Log.d("runOnUiThread","");
                      adapter.notifyDataSetChanged();
                    }

                });


            }

        });


    }
    @Override
    protected void onResume(){
        super.onResume();

        Log.d("onTextChanged","onResume");


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            default:
                break;
        }
    }
}
