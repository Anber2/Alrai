package com.mawaqaa.alrai.other;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by HP on 5/30/2017.
 */

public class SessionClass {

    public static NewsClass newsClassObj = new NewsClass();

    public static String popupFlag = "";
    public static String newsCatID = "";
    public static String newsSubCatID = "";
    public static String selectedCat = "";
    public static String URLlinkCat = "";
    public static final String EatndRun_PROFILEURI = "AlNasherUri";



    public static void BackupDatabase() throws IOException {
        boolean success = true;


        File file = null;
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "alraiBackup");
        //file = new File("/sdcard"+File.separator+"CeaserPlusBackup/");

        if (file.exists()) {
            success = true;
        } else {
            success = file.mkdir();
        }


        if (success) {
            String inFileName = "/data/data/com.mawaqaa.alrai/databases/alraiDB.db";
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory() + "/alraiBackup/alraiDB.db";
            //Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();
        }
    }

}
