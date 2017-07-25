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

import audiosteganography.fourier.Complex;

public class FFT {

    // compute the FFT of x[], assuming its length is a power of 2
    public static Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }

        // fft of even terms
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }

        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a power of 2
    public static Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
            y[i] = y[i].times(1.0 / N);
        }

        return y;

    }

    public static double[][] getMag(double[] data, int Fs) {
        data=correctDataLength(data);
        Complex[] x = new Complex[data.length];
        for (int i = 0 ; i<data.length ; i++) { //fill x with the data in complex form
                x[i] = new Complex(data[i], 0);
        }
        
        Complex[] y = fft(x); //get the fft of the data
        int n=y.length;

        double[] k = new double[n]; //for calculating the frequency     
        int j = 0;
        for (int i = -n/2 ; i<(n/2)-1 ; i++) { //populate k with integers from -n/2 to n/2-1
                k[j]=i;
                j++;    
        }

        double T=n/(double)Fs;

        double amp[] = new double[n];
        for (int i = 0; i < n; i++) {
           amp[i] = y[i].divides(new Complex(n,0)).abs(); //normalize data and get real magnitude           
          //amp[i] = y[i].abs();
        }

        fftShift(amp); //reorder array elements so they are in order from lowest to highest frequency

        //make multidimensional array of Freq & Amp
        double freqMag[][] = new double[n][2];
        for (int i = 0; i < n; i++) {
                
                freqMag[i][0]=k[i]/T; //the frequency
                freqMag[i][1]=amp[i]; //the 2nd element of the frequency array is the magnitude
                System.out.println("Freq: " + (k[i]/T) + ", Amp: " + amp[i]);
        }
        return freqMag;
            //System.out.println("Freq: " + (k[i]/T) + ", Amp: " + amp[i]);
    }

    public static void fftShift(double[] x) {
        double[] temp = new double[x.length];
        for (int i = 0 ; i<x.length ; i++) { //make temp array with same contents as x
                temp[i]=x[i];
        }

        for (int i = 0 ; i<x.length/2 ; i++) {
                x[i]=temp[x.length/2+i];
                x[x.length/2+i]=temp[i];
        }
    }

    public static double[] correctDataLength(double[] in) { //changes the length of in to a power of 2
        int n = in.length;
        double x = (int)(Math.log(n)/Math.log(2));
        if (Math.abs((x-(Math.log(n)/Math.log(2))))<.000001) { //this statement can really be improved
                return in;
        }

        int newLength = (int)Math.pow(2,x);
        double[] correctArray = new double[newLength];
        for (int i = 0 ; i<newLength-1 ; i++) {
                if (i<in.length) {
                        correctArray[i]=in[i];
                } else { //pad with 0s
                        correctArray[i]=0;
                }
        }
        return correctArray;
    }

    public static double[] getFreqs(int n, int fS) {
        double[] freqs = new double[n];
        double[] k = new double[n]; //for calculating the frequency     
        int j = 0;
        for (int i = -n/2 ; i<(n/2)-1 ; i++) { //populate k with integers from -n/2 to n/2-1
                k[j]=i;
                j++;    
        }

        double T=n/(double)fS;

        for (int i = 0 ; i<freqs.length ; i++) {
                freqs[i]=k[i]/T; //the frequency
        }

        //reorder array elements so they match the form given by the FFT
        //put beginning half at the end
        //put end half at beginning
        fftShift(freqs);
        return freqs;
    }

}
