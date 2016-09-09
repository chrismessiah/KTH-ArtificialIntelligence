		int[] obsSequence = getInputAsVector(stdin, line4);
	
        // get first column from inputMatrix
        double[][] delta1 = elementWiseProduct(piMatrix,getColumnFromMatrix(bMatrix, obsSequence[0]));

        /* 9 SEPTEMBER: 

        1. Write function to either transpose A (and then use getColumnFromMatrix)
            or a new getRowFromMatrix (maybe more clear)

        2. Do element-wise product between delta1 and A_row1, and take max element of each row

        */

        // delta2-matrix
        

        import java.util.Collections;


        //check which state j probably lead to transition to this state i
        // also multiply with b-vector. This row does all of the multiplications of page2
        temp_matrix = a_transposed * elementWiseProduct(delta1,b(obsSequence[1]));


        
        double[][] delta2_max_probability_vector;
        double[][] delta_argmax_state_vector;

        // take out max values from each row of the matrix
        for row in temp_matrix:

            delta2_max_probability_vector.append( Collections.max(row) )
        	// delta_argmax_state_vector.append( Collections.max(row) )



        // the max of each row of the above matrix becomes the delta2-vector called "max probability"
