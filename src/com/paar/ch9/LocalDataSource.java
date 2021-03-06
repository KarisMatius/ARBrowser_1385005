package com.paar.ch9;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import jp.androidgroup.nyartoolkit.R;


public class LocalDataSource extends DataSource{
    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap[] icons = new Bitmap[3];
    
    private static String[] descriptions = new String[4];
//    private static Bitmap icon = null;
    
    public LocalDataSource(Resources res) {
        if (res==null) throw new NullPointerException();
        
        createIcon(res);
    }
    
    protected void createIcon(Resources res) {
        if (res==null) throw new NullPointerException();
        
//        icon=BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
        icons[0] = BitmapFactory.decodeResource(res, R.drawable.machingunlily);
        icons[1] = BitmapFactory.decodeResource(res, R.drawable.marmot_s);
        icons[2] = BitmapFactory.decodeResource(res, R.drawable.elk_bull_s);
    }
    
    public List<Marker> getMarkers() {
//        Marker atl = new IconMarker("ATL", 39.931269, -75.051261, 0, Color.DKGRAY, icon);
//        cachedMarkers.add(atl);
//
//        Marker home = new Marker("Mt Laurel", 39.95, -74.9, 0, Color.YELLOW);
//        cachedMarkers.add(home);
    	
    	descriptions[0] = "The Columbian Ground Squirrel is the most commonly seen animal in the park during the summer.";
    	descriptions[1] = "Hoary Marmots are colonial animals that live in the alpine zone from 6,800 to 8,000 feet.";
    	descriptions[2] = "Elk are light brown with dark faces, necks and legs.";
    	descriptions[3] = "The Virginia Rail, Rallus limicola, is a small waterbird, of the family Rallidae.";

        Marker machingunlily = new IconMarker("Machingun Lily", 35.485881, 139.341032, 30, Color.DKGRAY, icons[0], descriptions[0]);
        cachedMarkers.add(machingunlily);
        
        Marker marmot = new IconMarker("Marmot", 35.486082, 139.341, 30, Color.DKGRAY, icons[1], descriptions[1]);
        cachedMarkers.add(marmot);

        Marker elk_bull = new IconMarker("Elk bull", 35.486231, 139.341016, 30, Color.DKGRAY, icons[2], descriptions[2]);
        cachedMarkers.add(elk_bull);
        
        Marker bison = new IconMarker("Bison", 35.484248, 139.342797, 30, Color.DKGRAY, icons[2], descriptions[3]);
        cachedMarkers.add(bison);
        
        return cachedMarkers;
    }
}