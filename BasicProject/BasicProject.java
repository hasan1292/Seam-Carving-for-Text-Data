//Code developed by Hasan Iqbal. Student of Tsinghua University. ID: 2016280141

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

public class BasicProject {

    public static void main(String[] args) throws Exception {
        
        try {
            String s;
            
            //For reading of text file.
            FileInputStream is;
            InputStreamReader ir;
            BufferedReader in;
            
            //Get input from user in command prompt via args[0]
            is = new FileInputStream(args[0]);
            ir = new InputStreamReader(is);
            in = new BufferedReader(ir);
            
            //Note down the start time of algorithm once data from text file is loaded!
            long start = System.nanoTime();
            
            System.out.println("\nProcessing...");
            FileOutputStream os;
            OutputStreamWriter or;
            BufferedWriter on;
            
            //Get the number of input file for making name of output file
            String[] outputName = args[0].split("-");
            
            //Create a output file name
            String fileName = outputName[0] + "-out.txt";
            File file = new File(fileName);
            
            os = new FileOutputStream(outputName[0] + "-out.txt");
            or = new OutputStreamWriter(os);
            on = new BufferedWriter(or);
            
            //Check if file already exists else make file
            if (!file.exists()) {
                file.createNewFile();
            }
            
            //read only the first line of input file to check parameters
            s = in.readLine();
            
            //split the first line of input file
            String[] tokens = s.split(" ");
            
            //store the rows number in m, columns number in n and number of seams required in k
            int m = Integer.parseInt(tokens[0]);
            int n = Integer.parseInt(tokens[1]);
            int k = Integer.parseInt(tokens[2]);
            
            //Declare a 2D Array for saving the original matrix from input file
            Integer A[][] = new Integer[m][n];
            
            //Read the rest of lines and save elements from txt file in Original Array (A)
            for (int i = 0; i < m; i++) {
                s = in.readLine();
                String[] tokens1 = s.split(" ");
                for (int j = 0; j < n; j++) {
                    A[i][j] = Integer.parseInt(tokens1[j]);
                }
            }
            
            
            /*
            Dynamic Programming Starts from here. This loop will run specified number of
            times(k). It will create a 2D array C to calculate the shortest lines with the
            lowest energies possible. Whereas 2D array D will save the direction i.e. where
            does the element got its minimum value (top-right, top, top-left). In the end a
            1D array minPath will be used for trackback.
            */
            
            //create a 2D array for saving the cumulative energies/values
            Integer C[][] = new Integer[m][n];
            
            //Create a 2D array for saving the direction
            Integer D[][] = new Integer[m - 1][n];
            
            //create a 1D array to save the minPath
            Integer minPath[] = new Integer[m];
            
            //run the outer most loop for k number of times specified by input file
            for (int times = 0; times < k; times++) {
                
                
                //save the first row of original Array A as it is in C
                for (int j = 0; j < n - times; j++)
                    C[0][j] = A[0][j];
                
                //run the for loop from 2nd row to mth row
                for (int i = 1; i < m; i++) {
                    for (int j = 0; j < n - times; j++) {
                        
                        //if the element is first. only check minimum from top and top-right
                        if (j == 0) {
                            if (C[i - 1][j] <= C[i - 1][j + 1]) {
                                C[i][j] = A[i][j] + C[i - 1][j];
                                D[i - 1][j] = j;
                            } else {
                                C[i][j] = A[i][j] + C[i - 1][j + 1];
                                D[i - 1][j] = j + 1;
                            }
                        }
                        
                        //if element is second. only check minimum from top and top-left
                        else if (j == (n - 1) - times) {
                            if (C[i - 1][j - 1] <= C[i - 1][j]) {
                                C[i][j] = A[i][j] + C[i - 1][j - 1];
                                D[i - 1][j] = j - 1;
                            } else {
                                C[i][j] = A[i][j] + C[i - 1][j];
                                D[i - 1][j] = j;
                            }
                        }
                        
                        //if element is middle, check minumum from top, top-left, top-right
                        else {
                            if (C[i - 1][j - 1] <= C[i - 1][j]
                                && C[i - 1][j - 1] <= C[i - 1][j + 1]) {
                                C[i][j] = A[i][j] + C[i - 1][j - 1];
                                D[i - 1][j] = j - 1;
                            }
                            
                            else if (C[i - 1][j] < C[i - 1][j - 1]
                                     && C[i - 1][j] <= C[i - 1][j + 1]) {
                                C[i][j] = A[i][j] + C[i - 1][j];
                                D[i - 1][j] = j;
                            }
                            
                            else if (C[i - 1][j + 1] < C[i - 1][j - 1]
                                     && C[i - 1][j + 1] < C[i - 1][j]) {
                                C[i][j] = A[i][j] + C[i - 1][j + 1];
                                D[i - 1][j] = j + 1;
                            }
                        }
                    }
                }
                
                //starting index to keep check of minimum value
                int lastMin = 0;
                
                //check the last row of calculated array and find the mimimum value
                for (int j = 1; j < n - times; j++) {
                    if (C[m - 1][j] < C[m - 1][lastMin])
                        lastMin = j;
                }
                
                minPath[m - 1] = lastMin;
                
                //backtrack for minimum path
                for (int i = m - 2; i >= 0; i--)
                    minPath[i] = D[i][minPath[i + 1]];
                
                int a = 0;
                
                //arrange the Array A in such a way that minPath elements are overwriten
                for (int i = 0; i < m; i++) {
                    a = 0;
                    for (int j = 0; j < n - times; j++) {
                        if (j != minPath[i]) {
                            A[i][a] = A[i][j];
                            a++;
                        }
                    }
                }
                
                /*
                Note: Size of Array A remains same, the only difference is I have over-written
                      the array leaving the last elements unused for next iteration of for-loop.
                      Same for the arrays C and D.
                */
            }
            
            //Output the Proccessed Array to the output file
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n - k; j++) {
                    on.write(A[i][j] + " ");
                }
                on.write("\n");
                
            }
            
            on.write("\n");
        
            //close the streams for input and output from text files
            on.close();
            in.close();
            
            //Note down the end time of whole procedure
            long end = System.nanoTime();
            
            System.out.println();
            
            //Calculate the total time taken
            long durationInMs = TimeUnit.MILLISECONDS.convert((end - start),TimeUnit.NANOSECONDS);
            
            System.out.println("Seam Carving Done! Please check "+ outputName[0] + "-out.txt file in source file folder!");
            
            System.out.println("\nTotal time spent by Seam Carving Algo= "+ durationInMs + " ms\n");
            
        } catch (Exception ex) {
            //Output exception if any occurs
            System.out.println("\nEither you have entered wrong file name or file does not exist in folder!\n");
        }
    }
    
}

