package com.dantastic.lelux;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ALL on 10/4/2016.
 */

public class BrightnessCommands {
    public List<BrightnessCommand> brightnessCommands;

    public BrightnessCommands (){
        //todo get from database
        brightnessCommands = new ArrayList<BrightnessCommand>();

       // brightnessCommands.add( new BrightnessCommand(BrightnessSetting.LIGHTEN,11,45, false));
       // brightnessCommands.add( new BrightnessCommand(BrightnessSetting.DARKEN,12,5, false));
    }

    public void Add(BrightnessCommand brightnessCommand){
        brightnessCommands.add(brightnessCommand);
    }

    public int Size(){
        return brightnessCommands.size();
    }

    public BrightnessCommand GetBrightnessCommand(BrightnessSetting brightnessSetting){
        Iterator<BrightnessCommand> itr=brightnessCommands.iterator();
        while(itr.hasNext()){
            BrightnessCommand bc = itr.next();
            if(bc.brightnessSetting == brightnessSetting){
                return bc;
            }
        }
        return null;
    }
}
