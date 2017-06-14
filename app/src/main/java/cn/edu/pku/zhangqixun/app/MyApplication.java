package cn.edu.pku.zhangqixun.app;

import android.app.Application;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cn.edu.pku.zhangqixun.bean.City;
import cn.edu.pku.zhangqixun.db.CityDB;

/**
 * Created by zhangqixun on 16/7/7.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyAPP";
    private static MyApplication mApplication;
    private CityDB mCityDB = null;
    private List<City> mCityList;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"MyApplicaiton->Oncreate");

        mApplication = this;
        mCityDB = openCityDB();
        initCityList();
    }

    public static MyApplication getInstance(){
        return  mApplication;
    }

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG,path);
        if (!db.exists()) {

            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.i("MyApp","mkdirs");
            }
            Log.i("MyApp","db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }

    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                prepareCityList();
            }
        }).start();

    }
    private boolean prepareCityList() {
        mCityList = mCityDB.getAllCity();
        int i=0;
        for (City city : mCityList) {
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        Log.d(TAG,"i="+i);
        return true;
    }
    public List<String> getCityList() {
        List<String> mStringList = new ArrayList<String>() ;

        for (City city : mCityList) {

            String cityName = city.getCity();
            mStringList.add(cityName);
        }
        return mStringList;
    }
    public String getCityCode(String name) {
        String code=null;

        for (City city : mCityList) {
            if(city.getCity().equals(name))
                code = city.getNumber();
        }
        return code;
    }
    public CityDB getmCityDB(){
        return mCityDB;
    }
}
