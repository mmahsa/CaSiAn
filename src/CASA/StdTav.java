/*
 * Copyright (C) 2016 Mahsa Moein
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package CASA;
import java.util.ArrayList;

public class StdTav {   
    int SectionID;
    ArrayList<Double> Tav = new ArrayList();
    ArrayList<Double> STD = new ArrayList();
    ArrayList<Integer> SignalID = new ArrayList();
 
    public double[] Tave;
    public double[] Stdev;
    public int[] SignalId;
    
    public boolean SortBasedOnTav()
    {
        if(!Tav.isEmpty() && !STD.isEmpty() && !SignalID.isEmpty())
        {
            
            this.Tave = Utils.convertDoubleArrayListToArray(Tav);
            this.Stdev = Utils.convertDoubleArrayListToArray(STD);
            this.SignalId = Utils.convertIntArrayListToArray(SignalID);
            
           boolean flag = true;   // set flag to true to begin first pass
           double tempTav;   //holding variable
           double tempStd;
           int tempSigId;
            while (flag) {
                flag = false;    //set flag to false awaiting a possible swap
                for (int j = 0; j < Tave.length - 1; j++) {
                    if (Tave[j] > Tave[j + 1]) // change to > for ascending sort
                    {
                        tempTav = Tave[j];                //swap elements
                        Tave[j] = Tave[j + 1];
                        Tave[j + 1] = tempTav;

                        tempStd = Stdev[j];                //swap elements
                        Stdev[j] = Stdev[j + 1];
                        Stdev[j + 1] = tempStd;

                        tempSigId = SignalId[j];                //swap elements
                        SignalId[j] = SignalId[j + 1];
                        SignalId[j + 1] = tempSigId;

                        flag = true;              //shows a swap occurred  
                    }
                }
            }
            return true;
        }
        else
            return false;
    }

}


