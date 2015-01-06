import java.net.*;
import java.io.*;
import com.nativelibs4java.opencl.*;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.opencl.util.*;
import com.nativelibs4java.util.*;
import org.bridj.Pointer;
import java.nio.ByteOrder;
import static org.bridj.Pointer.*;
import static java.lang.Math.*;
import java.io.IOException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.*;

public class CLFilter {
	
	public CLFilter(BufferedImage image) throws IOException {
		
		int[] image_data = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth() );
		
		CLContext context = JavaCL.createBestContext();
        CLQueue queue = context.createDefaultQueue();
        ByteOrder byteOrder = context.getByteOrder();
        
        int n = image_data.length;
        Pointer<Integer>
            image_data_ptr = allocateInts(n).order(byteOrder),
			filter_ptr = allocateInts(9).order(byteOrder);
		
		//set image_data to a pointer
        for (int i = 0; i < n; i++) {
			
			Color c = new Color(image_data[i]);
            image_data_ptr.set(i, c.getRed() );
        }
		//set up a high pass filter
		filter_ptr.set(0, -1);	filter_ptr.set(1, -1);	filter_ptr.set(2, -1);
		filter_ptr.set(3, 0);	filter_ptr.set(4, 0);	filter_ptr.set(5, 0);
		filter_ptr.set(6, 1);	filter_ptr.set(7, 1);	filter_ptr.set(8, 1);
		
        // Create OpenCL input buffers (using the native memory pointers aPtr and bPtr) :
        CLBuffer<Integer> 
            image_data_ptr_buffer = context.createIntBuffer(Usage.Input, image_data_ptr),
			filter_ptr_buffer = context.createIntBuffer(Usage.Input, filter_ptr);
            

        // Create an OpenCL output buffer :
        CLBuffer<Integer> //out = context.createIntBuffer(Usage.Output, n),
			out_image_data = context.createIntBuffer(Usage.Output, n);

        // Read the program sources and compile them :
        String src = IOUtils.readText(new File("TutorialKernels.cl").toURI().toURL());
        CLProgram program = context.createProgram(src);

		double start = System.nanoTime();
		
        // Get and call the kernel :
        CLKernel addFloatsKernel = program.createKernel("high_pass");
		//__kernel void high_pass(__global int* color_out, __global int* filter, int image_size, int filter_width, int image_width, __global int* orginal_color)
        addFloatsKernel.setArgs(out_image_data, filter_ptr_buffer, n, 3, image.getWidth(), image_data_ptr_buffer);
        int[] globalSizes = new int[] { n };
        CLEvent addEvt = addFloatsKernel.enqueueNDRange(queue, globalSizes);
        
        Pointer<Integer> out_image_data_ptr = out_image_data.read(queue, addEvt); // blocks until add_floats finished
	
		double end = (System.nanoTime() - start)/1000000000;
		System.out.println("Took " + end + " seconds");
        // Print the first 10 output values :
        //for (int i = 0; i < 1024 && i < n; i++)
            //System.out.println("out[" + i + "] = " + out_image_data_ptr.get(i) );
	}

    public static void main(String[] args)  {
		try {
			new CLFilter(ImageIO.read(new File("image.jpg") ) );
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
        
        
    }
}