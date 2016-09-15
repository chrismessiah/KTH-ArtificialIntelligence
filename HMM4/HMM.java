
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.lang.Math;


public class HMM {
    public double[][] A;
    public double[][] B;
    public double[][] pi;
    public int maxIters;
    public double[] ct_array;
    

    // constructor
    public HMM(double[][] aMat,double[][] bMat,double[][] piMat) {
        A = aMat;
        B = bMat;
        pi = piMat;
        maxIters = 70;
    }

    // calc alpha then calc beta then update().
    public void baum_welch(int[] obsSequence) {

        double[][] oldA;
        double[][] oldB;
        double[][] oldPi;
        
        int iters = 0;
        double oldLogProb = 0;
        double logProb = 1;
        double threshold = 0.00005;

        while (iters < maxIters){
        //while (iters < maxIters && logProb > oldLogProb+threshold){
            // 7 to iterate or not to iterate...
            oldA = A;
            oldB = B;
            oldPi = pi;
            //System.out.println(oldLogProb);

            double[][] alphaMatrix = alpha_forward_pass(obsSequence);
            double[][] betaMatrix = beta_backward_pass(obsSequence);
            reEstimate_lambda(obsSequence, alphaMatrix, betaMatrix);

            logProb = calcLogProb(obsSequence);

            /*
            if (logProb > oldLogProb){
                oldLogProb = logProb;
                iters++;    
            } else {
                iters = maxIters;
            }*/
            iters++;
            
            //System.out.println(logProb-oldLogProb);
        }
        // when at this state, the A, B and pi are not improving anymore
    }

    // sum all alphas in one sequence to get the probability of the sequence
    public double calcLogProb(int[] obsSequence){
        int T = obsSequence.length;
        double logProb = 0;
        for (int i=0;i<T;i++){
            logProb = logProb + Math.log10(ct_array[i]);
        }
        return logProb;
    }



    public double[][] alpha_forward_pass(int[] obsSequence){

        int N = A[0].length; // 4
        int T = obsSequence.length; // 1000
        ct_array = new double[T];

        double[][] alpha = new double[N][T];


        // compute alpha0(i)
        double c0 = 0;
        for (int i=0;i<N;i++){
            alpha[i][0] = pi[i][0] * B[i][0];
            c0 = c0 + alpha[i][0];
        }
        ct_array[0] = 1/c0;

        double ct;
        // scale the alpha0(i)
        for (int t=1;t<T;t++){
            ct = 0;
            for (int i=0;i<N;i++){
                alpha[i][t] = 0;
                for(int j=0;j<N;j++){
                    alpha[i][t] = alpha[i][t] + alpha[j][t-1] * A[j][i];
                }
                alpha[i][t] = alpha[i][t] * B[i][obsSequence[t]];
                ct = ct + alpha[i][t];
            }

            // scale alpha_t(i)
            ct = 1/ct;
            for(int i=0;i<N;i++){
                alpha[i][t] = ct * alpha[i][t];
            }
            ct_array[t] = ct;
        }
        return alpha;
    }



    // estimate betaMatrix
    public double[][] beta_backward_pass(int[] obsSequence){

        int N = A[0].length; // 4
        int T = obsSequence.length; // 1000
        
        double[][] beta = new double[N][T];

        // let betaT-1(i) = 1, scaled by cT-1
        for (int i=0;i<N;i++){
            beta[i][T-1] = ct_array[T-1];
        }

        // beta-pass
        for (int t=T-2;t>=0;t--){
            for (int i=0;i<N;i++){
                beta[i][t] = 0;
                for (int j=0;j<N;j++){
                    beta[i][t] = beta[i][t] + A[i][j] * B[j][obsSequence[t+1]] * beta[j][t+1];
                }
                // scale beta_t(i) with the same scale factor as alpha_t(i)
                beta[i][t] = ct_array[t] * beta[i][t];
            }
        }
        return beta;
    }



    public void reEstimate_lambda(int[] obsSequence, double[][] alpha, double[][] beta){

        int N = A[0].length; // 4
        int T = obsSequence.length; // 1000
        int M = B[0].length;

        double[][] gamma = new double[N][T];
        double[][][] diGamma = new double[N][N][T]; // i j t

        // difficult with dimensions of diGamma

        // compute diGamma_t(i,j) and gamma_t(i)
        for(int t=0;t<T-1;t++){
            double denom = 0;
            for (int i=0;i<N;i++){
                for (int j=0;j<N;j++){
                    denom = denom + alpha[i][t] * A[i][j] * B[j][obsSequence[t+1]] * beta[j][t+1];
                }
            }
            for (int i=0;i<N;i++){
                gamma[i][t] = 0;
                for (int j=0;j<N;j++){
                    diGamma[i][j][t] = (alpha[i][t] * A[i][j] * B[j][obsSequence[t+1]] * beta[j][t+1]) / denom;
                    gamma[i][t] = gamma[i][t] + diGamma[i][j][t];
                }
            }
        }

        //special case for last gamma 
        double denom = 0;
        for (int i=0;i<N;i++){
            denom = denom + alpha[i][T-1];
        }
        for (int i=0;i<N;i++){
            gamma[i][T-1] = alpha[i][T-1] / denom;
        }


        // re-estimate A, B, pi

        // re-estimate pi
        for (int i=0;i<N;i++){
            pi[i][0] = gamma[i][0];
        }

        // re-estimate A
        for (int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                double numer = 0;
                denom = 0;
                for (int t=0;t<T-1;t++){
                    numer = numer + diGamma[i][j][t];
                    denom = denom + gamma[i][t];
                }
                A[i][j] = numer / denom;
            }
        }

        // re-estimate B
        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                double numer = 0;
                denom = 0;
                for(int t=0;t<T;t++){
                    if(obsSequence[t] == j){
                        numer = numer + gamma[i][t];
                    }
                    denom = denom + gamma[i][t];
                }
                B[i][j] = numer / denom;
            }
        }
    }




}