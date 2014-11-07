import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.*;

public class FilterImage {
	
	JFrame imageFrame = new JFrame("Images");
    JFrame editorFrame = new JFrame("Editor");
    BufferedImage changed_image = null;
    BufferedImage image = null;
    
    JTextField matrix_size_text_fieled = null;
    JPanel matrix_panel = new JPanel();
    JTextField resize_text_field = null;
    JPanel image_panel = new JPanel();
	
	/*private int filter[] = 
	{
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,-624,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
	};*/
    /*
	private int filter[] = {
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,-80,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
	};
    */
  public static void main(String[] args) throws Exception
  {
    new FilterImage();
  }

  public FilterImage() throws Exception
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        
        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        imageFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        imageFrameLayout(openFile());
        editorFrameLayout();
        
        editorFrame.pack();
        editorFrame.setLocationRelativeTo(imageFrame);
        editorFrame.setVisible(true);

        imageFrame.pack();
        imageFrame.setLocationRelativeTo(null);
        imageFrame.setVisible(true);
      }
    });
  }
 
  
  public File openFile(){
		final JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		
		int r = fc.showOpenDialog(editorFrame.getRootPane());
		if(r == JFileChooser.APPROVE_OPTION){
			File f = fc.getSelectedFile();
			return f;
		}else{
			System.err.println("FILE OPEN CALCELLED");
		return null;
		}
  }
  
  /**
   * editorFrameLayout	-	Creates a layout for the editor frame
   */
  private void editorFrameLayout() {
	  JLabel matrix_size_label = new JLabel("Matrix Size:");
	  matrix_size_text_fieled = new JTextField();
	  JButton matrix_size_apply = new JButton("Apply");
	  matrix_size_apply.addActionListener(new MatrixSizeListener());
	  
	  JPanel matrix_size_panel = new JPanel(new GridLayout(1, 3));
	  matrix_size_panel.add(matrix_size_label);
	  matrix_size_panel.add(matrix_size_text_fieled);
	  matrix_size_panel.add(matrix_size_apply);
	  
	  JPanel resize_outer_panel = new JPanel(new BorderLayout());
	  JPanel resize_inner_panel = new JPanel(new GridLayout(1, 2));
	  
	  resize_text_field = new JTextField();
	  JButton resize_button = new JButton("Resize Picture");
	  resize_button.addActionListener(new ResizePicture());
	  
	  resize_inner_panel.add(new JLabel("Resize value:") );
	  resize_inner_panel.add( resize_text_field );
	  
	  JButton both_hist_btn = new JButton("Find Plaque");
	  both_hist_btn.addActionListener(new BothHistListener());
	  
	  JButton horz_hist_btn = new JButton("Horz Histogram");
	  horz_hist_btn.addActionListener(new HorzHistListener());
	  
	  JButton vert_hist_btn = new JButton("Vert Histogram");
	  vert_hist_btn.addActionListener(new VertHistListener());
	  
	  resize_outer_panel.add(resize_inner_panel, BorderLayout.NORTH);
	  
	  resize_outer_panel.add(both_hist_btn, BorderLayout.SOUTH);
	  resize_outer_panel.add(horz_hist_btn, BorderLayout.EAST);
	  resize_outer_panel.add(vert_hist_btn, BorderLayout.CENTER);
	  
	  
	  editorFrame.getContentPane().add(matrix_size_panel, BorderLayout.NORTH);
	  editorFrame.getContentPane().add(matrix_panel, BorderLayout.CENTER);
	  editorFrame.getContentPane().add(resize_outer_panel, BorderLayout.SOUTH);
  }
  /**
   * imageFrameLayout 	-	creates a layout for the frame with the images
   * @param filename	-	the image filename
   */
  private void imageFrameLayout(File file) {
	  
		
      try {
        image = ImageIO.read(file);
		changed_image = image;
        //changed_image = convertToGrayScale(image); 
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
      ImageIcon imageIcon = new ImageIcon(image);
      JLabel jLabel = new JLabel();
      jLabel.setIcon(imageIcon);
	  /*ImageIcon changed_icon = new ImageIcon(changed_image);
	  JLabel j_label = new JLabel();
      j_label.setIcon(changed_icon);*/
      
      image_panel = new JPanel( new BorderLayout() );
      image_panel.add(jLabel, BorderLayout.NORTH);
      //image_panel.add(j_label, BorderLayout.SOUTH);     
      
      imageFrame.add(image_panel);
  }
  
  /**
   * filterImage	-	applies a filter to the old image
   * @param first	-	the old image
   * @param filter	-	the filter to be applied, can be low or high pass
   * @return	The new image with a filter applied to it
   */
  private BufferedImage filterImage( BufferedImage first, int[] filter ) {
	long start = System.nanoTime();
	  BufferedImage old = deepCopy(first);
	  int[] old_rgb = old.getRGB(0,0, old.getWidth(), old.getHeight(), null, 0, old.getWidth());
	  int[] new_rgb = old.getRGB(0,0, old.getWidth(), old.getHeight(), null, 0, old.getWidth());
	  
	  for ( int i = 0; i < new_rgb.length; i++ ) {
		  new_rgb[i] = (int)filterImageAux(old_rgb, i, filter, old.getWidth(), old.getHeight());
	  }
	  
	  old.setRGB(0,0,old.getWidth(), old.getHeight(),new_rgb, 0, old.getWidth());
	  float end = (System.nanoTime() - start)/1000000000.0f;
	  System.out.println("Took : " + end + "s to filter");
	  return old;
  }
  /**
   * filterImageAux - 	given the starting pixel it will apply a filter
   * 
   * @param rgb		-	the color values	
   * @param center	-	the center pixel
   * @param filter	-	the filter to apply
   * @param img_width	-	the old images width
   * @param img_height	-	the old images height
   * @return	An averaged value for the pixel
   */
  private double filterImageAux(int[] rgb, int center, int[] filter, int img_width, int img_height) {;
  	  int sum = 0;  
	  int x_filter = 0;
	  int y_filter = 0;
	  			// move up															move left
	  int start = center - ( ( (int)Math.sqrt(filter.length) -1 ) / 2 )*img_width - ( (int)Math.sqrt(filter.length) -1 ) / 2;
	  //check if with in the bounds of the image
	  if ( start < 0 ) {
		  return rgb[center];
	  } else if ( Math.sqrt(filter.length) + start + Math.sqrt(filter.length)*img_width > img_width*img_height  ) {
		  return rgb[center];
	  }
	  
	  
	  for ( int x = start; x < Math.sqrt(filter.length) + start; x++ ) {
		  y_filter = 0;
		  for ( int y = 0; y < Math.sqrt(filter.length)*img_width; y+= img_width ) {
			  Color c = new Color(rgb[x+y]);
			  sum += c.getRed() * filter[x_filter+y_filter];		  
			  y_filter+=Math.sqrt(filter.length);
		  }
		  x_filter++;
	  }
	 
	  //checks for different possible sums
	  int temp = sum;
	  if ( arraySum(filter) != 0 ) { 
		temp = (int)((double)sum / (double)arraySum(filter)); 
	  }
	  if ( temp < 0 ) {
		  temp = 0;
	  }
	  
	  if ( temp > 255 ) {
		  temp = 255;
	  }
	  
	  return new Color(temp,temp,temp).getRGB();
  }
  /**
   * arraySum - sums a given array
   * @param array	The array to sum
   * @return	An int of the summed array
   */
  private int arraySum(int[] array ) {
	  int sum = 0;
	  for ( int i = 0; i < array.length; i++) {
		  sum += array[i];
	  }
	  return sum;
  }
  /**
   * convertToGrayScale - converts the image to grayscale
   * @param first	The old image
   * @return	A new image with grayscale color
   */
  private BufferedImage convertToGrayScale(BufferedImage first ) {
	  BufferedImage old = deepCopy(first);
	  int[] new_rgb = old.getRGB(0,0, old.getWidth(), old.getHeight(), null, 0, old.getWidth());
	  
	  for ( int i = 0; i < new_rgb.length; i++ ) {
		  Color c = new Color(new_rgb[i]);
		  int red = (int)(c.getRed() * 0.299);
		  int green =(int)(c.getGreen() * 0.587);
		  int blue = (int)(c.getBlue() *0.114);
		  c = new Color(red+green+blue, red+green+blue,red+green+blue);
		  new_rgb[i] = c.getRGB();
	  }
	  
	  old.setRGB(0,0,old.getWidth(), old.getHeight(),new_rgb, 0, old.getWidth());
	  return old;
  }
  	/**
  	 * deepCopy - takes an image and does a deep copy
  	 *  
  	 * @param bi	The image to copy
  	 * @return	The deep copied image
  	 */
	private BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	/**
	 * redrawImageFrame - when an event happens to change the new image
	 */
	public void redrawImageFrame() {
		ImageIcon imageIcon = new ImageIcon(changed_image);
	    JLabel jLabel = new JLabel();
		jLabel.setIcon(imageIcon);
		/*ImageIcon changed_icon = new ImageIcon(changed_image);
		JLabel j_label = new JLabel();
	    j_label.setIcon(changed_icon);*/
	    
	    image_panel.removeAll();
	    
	    image_panel.add(jLabel, BorderLayout.NORTH);
	    //image_panel.add(j_label, BorderLayout.SOUTH);   
		
		imageFrame.pack();
		imageFrame.revalidate();
		imageFrame.repaint();
	}
	/**
		Counting rows from top to bottom
	*/
	public float[] histVert(int width, int height) {
		//System.out.println("==========Vertical Lines==========");
		int count = 0;
		float array[] = new float[height];
		for(int y=0;y<height-1;y++){
			count=0;
			for(int x=0;x<width-1;x++){
				Color c = new Color(changed_image.getRGB(x, y));
				/*if ( 	c.getRed() >= 75 && c.getRed() <= 85 &&
							c.getGreen() >= 80 && c.getGreen() <= 100 &&
							c.getBlue() >= 90 && c.getBlue() <= 110 ) {	
							
								count++;
							
					}*/
					if ( c.getRed() > 250 ) {
						count++;
					}
			}
			//count used here
			float percent = ( (float)count/(float)width ) * 100;
			//System.out.println(percent);
			array[y] = percent;
		}
		
		return removeModeAndLess(array);
	}
	/**
		Counting columns from left to right
	*/
	public float[] histHoriz(int width, int height) {
		//System.out.println("==========Horizontal Lines==========");
		float array[] = new float[width];
		int count = 0;
		for(int x=0;x<width-1;x++){
			count=0;
			for(int y=0;y<height-1;y++){
				Color c = new Color(changed_image.getRGB(x, y));
				/*if ( 	c.getRed() >= 75 && c.getRed() <= 85 &&
							c.getGreen() >= 80 && c.getGreen() <= 100 &&
							c.getBlue() >= 90 && c.getBlue() <= 110 ) {	
							
								count++;
							
					}*/
					if ( c.getRed() > 250 ) {
						count++;
					}
			}
			//count used here
			float percent = ( (float)count/(float)height ) * 100;
			//System.out.println(percent);
			array[x] = percent;
		}
		
		return removeModeAndLess(array);
	}
	/**
		Finds and sets all values to 0 that are equal to or less than the
		mode of the given array;
		
		@param old	float[] - Contains the array of values to check
		
		@return A new float[] of removed values
	*/
	private float[] removeModeAndLess(float old[]) {
		ArrayList<Float> values = new ArrayList<>();
		ArrayList<Integer> counts = new ArrayList<>();
		values.add(old[0]);
		counts.add(1);
		int old_length = old.length;
		//finds the count of percents
		for ( int i = 1; i < old_length; i++ ) {
			float temp_value = old[i];
			for ( int j = 0; j < values.size(); j++ ) {
				if ( values.get(j) == temp_value ) {
					counts.set(j, counts.get(j) + 1 );
					break;
				}
			}
			if ( values.contains(temp_value) == false ) {
				values.add(temp_value);
				counts.add(1);
			}
		}
		
		//For printing
		/*
		for ( int i = 0; i < values.size(); i++ ) {
			System.out.println(values.get(i) + " occur " + counts.get(i) );
		}
		*/
		
		//get mode
		int mode_index = 0;
		int mode_greatest = counts.get(0);
		for ( int i = 1; i < values.size(); i++ ) {
			if ( counts.get(i) > mode_greatest ) {
				mode_greatest = counts.get(i);
				mode_index = i;
			}
		}
		
		//remove all mode and less than
		float mode = values.get(mode_index);
		float new_array[] = old;
		for ( int i = 0; i < old.length; i++ ) {
			if ( old[i] <= mode ) {
				new_array[i] = 0.0f;
			}
		}
		return new_array;
	}
	
	/**
		Finds the plaque from horizontal and vertical histogram percent data

		@param	horz_percent	The percentage of black pixels in each column
		@param	vert_percent	The percentage of black pixels in each row
	*/
	public void findPlaque(float horz_percent[], float vert_percent[], int width, int height) {
		ArrayList<int[]> horz_lines = new ArrayList<>();		//the inner itn arrays are len 2, {pos, length}
		ArrayList<int[]> vert_lines = new ArrayList<>();
		//find for horizontal
		int start = 0;
		int len = 0;
		for ( int i = 0; i < width-1; i++ ) {
			if ( horz_percent[i] > 0.0 || horz_percent[i+1] > 0 ) {
				len++;
			} else if ( horz_percent[i] == 0.0 && len > 50 ) {
				int container[] = new int[2];
				container[0] = start;
				container[1] = len;
				horz_lines.add(container);
				start = i;
				len = 1;
			} else {
				start = i;
				len = 1;
			}
		}
		//find for vertical
		start = 0;
		len = 0;
		for ( int i = 0; i < height-1; i++ ) {
			if ( vert_percent[i] > 0.0 ||  vert_percent[i+1] > 0.0) {
				len++;
			} else if ( vert_percent[i] == 0.0 && len > 50 ) {
				int container[] = new int[2];
				container[0] = start;
				container[1] = len;
				vert_lines.add(container);
				start = i;
				len = 1;
			} else {
				start = i;
				len = 1;
			}
		}
		
		//TESTING
		
		System.out.println("For Horizontal Line(s)");
		for ( int i = 0; i < horz_lines.size(); i++ ) {
			int container[] = horz_lines.get(i);
			System.out.println("pos= " + container[0] + " length= " + container[1] );
		}
		System.out.println("For Vertical Line(s)");
		for ( int i = 0; i < vert_lines.size(); i++ ) {
			int container[] = vert_lines.get(i);
			System.out.println("pos= " + container[0] + " length= " + container[1] );
		}
		
		/*if ( horz_lines.size() != 1 ) {
			if ( top_center_value > 9 ) {
				System.out.println("Horz looped 10 times, exiting");
				System.exit(-1);
			}
			System.out.println("running horz again");
			initFindPlaque(width, height,1,0);
			return;
		} else if ( vert_lines.size() != 1 ) {
			if ( left_center_value > 9 ) {
				System.out.println("Vert looped 10 times, exiting");
				System.exit(-1);
			}
			System.out.println("running vert again");
			initFindPlaque(width, height,0,1);
			return;
		}*/
		
		if ( horz_lines.size() > 0 && vert_lines.size() > 0 ) {
			int array_pos[] = compareLinesToColorDB(horz_lines, vert_lines);
			
			int container[] = horz_lines.get(array_pos[0]);	
			int start_x = container[0];
			int plaque_width = container[1];
			
			container = vert_lines.get(array_pos[1]);
			int start_y = container[0];
			int plaque_height = container[1];
			
			int new_rgb[] = new int[ (plaque_width) * (plaque_height)];
			int new_rgb_counter = 0;
			for ( int y = start_y; y < plaque_height+start_y; y++ ) {
				for ( int x = start_x; x < plaque_width+start_x; x++ ) {
					new_rgb[new_rgb_counter] = image.getRGB(x,y);
					new_rgb_counter++;
				}
			}
			changed_image = new BufferedImage(plaque_width, plaque_height, BufferedImage.TYPE_INT_RGB);
			changed_image.setRGB(0, 0, plaque_width, plaque_height, new_rgb, 0, plaque_width );
			redrawImageFrame();
		} else {
			System.out.println(	"ERROR: Could not find plaque, found " + horz_lines.size() + 
								" horizontal lines and found " + vert_lines.size() + " vertical lines.");
		}
	}
	/**
		Compares the plaque lines found to the color positions found
		
		@return 	A int[] containing the array pos for horz_lines and vert_lines
	*/
	private int[] compareLinesToColorDB(ArrayList<int[]> horz_lines, ArrayList<int[]> vert_lines ) {
		FindPlaque fp = new FindPlaque(image);
		int horz_count[] = new int[horz_lines.size()];
		int vert_count[] = new int[vert_lines.size()];
		
		for ( int i = 0; i < horz_lines.size(); i++ ) {
			horz_count[i] = 0;
			for ( int j = 0; j < fp.positions.size(); j++ ) {
				if ( fp.positions.get(j)[0] > horz_lines.get(i)[0] && fp.positions.get(j)[0] < horz_lines.get(i)[0] + horz_lines.get(i)[1] ) {
					horz_count[i]++;
				}
			}
		}
		
		for ( int i = 0; i < vert_lines.size(); i++ ) {
			horz_count[i] = 0;
			for ( int j = 0; j < fp.positions.size(); j++ ) {
				if ( fp.positions.get(j)[1] > vert_lines.get(i)[0] && fp.positions.get(j)[1] < vert_lines.get(i)[0] + vert_lines.get(i)[1] ) {
					vert_count[i]++;
				}
			}
		}
		
		int horz_index = 0;
		int horz_max = 0;
		for ( int i = 0; i < horz_count.length; i++ ) {
			if ( horz_max < horz_count[i] ) {
				horz_max = horz_count[i];
				horz_index = i;
			}
		}
		int vert_index = 0;
		int vert_max = 0;
		for ( int i = 0; i < vert_count.length; i++ ) {
			if ( vert_max < vert_count[i] ) {
				vert_max = vert_count[i];
				vert_index = i;
			}
		}
		int array[] = {horz_index, vert_index};
		return array;
	}
	
	public void histogram(String dir){		
		int width,height,count,rgb,grey;
		float array[] = new float[1];
		width = changed_image.getWidth();
		height = changed_image.getHeight();
		
		int[][] histogram = new int[width][height];
		
		if(dir.equals("vert")){
			System.out.println("==========Vertical==========");
			array = new float[height];
			
			for(int y=0;y<height-1;y++){
				count=0;
				for(int x=0;x<width-1;x++){
					Color c = new Color(changed_image.getRGB(x, y));
					/*if ( 	c.getRed() >= 75 && c.getRed() <= 85 &&
							c.getGreen() >= 80 && c.getGreen() <= 100 &&
							c.getBlue() >= 90 && c.getBlue() <= 110 ) {	
							
								count++;
							
					}*/
					if ( c.getRed() > 250 ) {
						count++;
					}
				}
				float percent = ( (float)count/(float)width ) * 100;
				array[y] = percent;
				//System.out.println(percent);
				for(int x=0;x<width-1;x++){
					if(x<=count){
						histogram[x][y] = 0;
					}else{
						histogram[x][y] = 255;
					}
				}
			}
		}else if(dir.equals("horz")){
			System.out.println("==========Horizontal==========");
			array = new float[width];
			
			for(int x=0;x<width-1;x++){
				count=0;
				for(int y=0;y<height-1;y++){
					/*if(Math.abs(changed_image.getRGB(x, y)>>16) > 250){
						count ++;
					}*/
					Color c = new Color(changed_image.getRGB(x, y));
					/*if ( 	c.getRed() >= 75 && c.getRed() <= 85 &&
							c.getGreen() >= 80 && c.getGreen() <= 100 &&
							c.getBlue() >= 90 && c.getBlue() <= 110 ) {	
							
								count++;
							
					}*/
					if ( c.getRed() > 250 ) {
						count++;
					}
				}
				
				float percent = ( (float)count/(float)height ) * 100;
				//System.out.println(percent);
				array[x] = percent;
				
				for(int y=0;y<height-1;y++){
					if(y<=count){
						histogram[x][y] = 0;
					}else{
						histogram[x][y] = 255;
					}
				}
			}
		}	else if ( dir.equals("both") ) {
			initFindPlaque(width, height,1,1);
			return;
		}		
		
		array = removeModeAndLess(array);
		for ( int i = 0; i < array.length; i++ ) {
			System.out.println(array[i]);
		}
		//System.out.println("Printing Histogram");
		for(int x=0;x<width-1;x++){
			for(int y=0;y<height-1;y++){
				rgb = new Color(255,255,255).getRGB();
				if ( x < array.length && array[x] > 0.0f && dir.equals("horz") ) {
					rgb = (histogram[x][y] << 16) + (histogram[x][y] << 8) + histogram[x][y];
				} else if ( y < array.length && array[y] > 0.0f && dir.equals("vert") ) {
					rgb = (histogram[x][y] << 16) + (histogram[x][y] << 8) + histogram[x][y];
				}
				changed_image.setRGB(x, y, rgb);
			}
		}
		
		
		
		
		redrawImageFrame();
		
		//int rgb = (gray << 16) + (gray << 8) + gray;
	}
	private int top_center_value = 0;
	private int left_center_value = 0;
	public void initFindPlaque(int width, int height, int incr_top, int incr_left ) {
		top_center_value+=incr_top;
		left_center_value+=incr_left;
		int top_filter[] = {	-1,-top_center_value,-1,
								 0, 0, 0,
								 1, top_center_value, 1 };
		int left_filter[] = {	-1, 0, 1,
								-left_center_value, 0, left_center_value,
								-1, 0, 1 };
		int right_filter[] = {	1, 0, -1,
								2, 0, -2,
								1, 0, -1 };
		int high_pass_filter[] = {	-1, -1, -1,
									-1, 8, -1,
									-1, -1, -1 };
		//horizontal
		changed_image = filterImage(image, top_filter);
		float horz_percent[] = histHoriz(width, height);
		//vertical
		changed_image = filterImage(image, left_filter);
		float vert_percent[] = histVert(width, height);
		findPlaque(horz_percent, vert_percent, width, height);
		return;
	}
	
	
//=================================================================================================
//	Events
	
	/**
	 * 	MatrixSizeListener - A listener that will act when the matrix button is clicked.
	 * 		It will create a square matrix depending on what number was entered.
	 */
	public class MatrixSizeListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent ae) {
		int matrix_size = 0;
		try {
			matrix_size = Integer.parseInt( matrix_size_text_fieled.getText().trim() );
			
			matrix_panel.removeAll();
			matrix_panel.setLayout( new BorderLayout() );
			
			JPanel text_field_panel = new JPanel( new GridLayout( matrix_size, matrix_size ) );
			for ( int x = 0; x < matrix_size; x++ ) {
				for ( int y = 0; y < matrix_size; y++ ) {
					text_field_panel.add(new JTextField());
				}
			}
			
			JButton apply_pic_btn = new JButton("Apply to Picture");
			apply_pic_btn.addActionListener(new ApplyToPicture());
			
			matrix_panel.add(text_field_panel, BorderLayout.NORTH);
			matrix_panel.add(apply_pic_btn, BorderLayout.SOUTH);
			
			editorFrame.pack();
			editorFrame.revalidate();
			editorFrame.repaint();
			
		}catch(Exception e) {
			System.out.println("Error on matrix size input: " + e.getMessage() );
		}
	}
		
	}
	
	/**
	 * 	ApplyToPicture - is a listener for the apply matrix to image button.
	 * 		It will take the matrix entered and apply it to the image.
	 */
	public class ApplyToPicture implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			try {
				
				JPanel temp_panel = (JPanel)matrix_panel.getComponent(0);	//list of text fields
				
				int count = temp_panel.getComponentCount();
				int[] filter = new int[count];
				count = (int)Math.sqrt(count);
				
				for ( int x = 0; x < count; x++ ) {
					for ( int y = 0; y < count; y++ ) {
						JTextField temp_text_field = (JTextField)temp_panel.getComponent(x*count + y);
						filter[x*count + y] = Integer.parseInt(temp_text_field.getText().trim());
					}
				}
				
				
			    
				changed_image = filterImage(image, filter);
				redrawImageFrame();
				
			}catch(Exception e) {
				System.out.println("Error on matrix input: " + e.getMessage() );
			}
		}
		
	}
	

	/**
	 * 	ResizePicture - is a listener for the resize button.
	 * 		It will either shrink or grow the image depending on if
	 * 		the number entered is less than 1, it will shrink,
	 * 		if greater than 1 it will grow.
	 */
	public class ResizePicture implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			try {
				double resize = Double.parseDouble( resize_text_field.getText() );

				int[] old_rgb = image.getRGB(0,0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
				
				int width = (int)(image.getWidth() * resize);		
				int height = (int)(image.getHeight() * resize);		
				int width_division = image.getWidth()/width;		
				int height_division = image.getHeight()/height;		
				
				int[] new_rgb = new int[width*height];

				System.out.println("Resized: " + width + "x" + height);
				
				if ( resize > 1 ) {
					for ( int x = 0; x < image.getWidth(); x++ ) {
						for ( int y = 0; y < image.getHeight(); y++ ) {
							new_rgb = growImage(old_rgb, resize, x, y, width, height, new_rgb, image.getWidth());
						}
					}
				} else if ( resize > 0 ) {
					for ( int x = 0; x < width; x++ ) {
						for ( int y = 0; y < height; y++ ) {
							new_rgb[x+y*width] =  averageShrink(old_rgb, width_division, height_division, x, y, image.getWidth(), image.getHeight());
						}
					}
				}
				changed_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				changed_image.setRGB(0, 0, width, height, new_rgb, 0, width );
				redrawImageFrame();
			} catch (Exception e) {
				System.out.println("Error on Resize input: " + e.getMessage() );
			}
		}
		/**
		 * 	growImage 		- 	a method to take a portion of the image and expand it
		 * 
		 * @param rgb		- 	the colour values
		 * @param resize	- 	the amount the image will be resized
		 * @param pos_x		-	start position in the x
		 * @param pos_y		-	start position in the y
		 * @param width		-	width of the new image
		 * @param height	-	height of the new image
		 * @param new_rgb	-	the new image
		 * @param image_width	-	the old image width
		 * @return
		 */
		private int[] growImage(int[] rgb, double resize, int pos_x, int pos_y, int width, int height, int[] new_rgb, int image_width ) {
			Color c = new Color(rgb[pos_x + pos_y*image_width]);
			int[] rgb_copy = new_rgb;
			for ( int x = pos_x*(int)resize; x < pos_x*(int)resize + (int)resize; x++ ) {
				for ( int y = pos_y*width*(int)resize; y < pos_y*width*(int)resize + width*(int)resize; y+= width ) {
					rgb_copy[x+y] = c.getRGB(); 
				}
			}
			
			return rgb_copy;
		}
		/**
		 * 	averageShrink 	- 	a method to shrink the image with an averaged sum
		 * 
		 * @param rgb		- 	the colour values
		 * @param resize	- 	the amount the image will be resized
		 * @param pos_x		-	start position in the x
		 * @param pos_y		-	start position in the y
		 * @param width		-	width of the new image
		 * @param height	-	height of the new image
		 * @param new_rgb	-	the new image
		 * @param image_width	-	the old image width
		 * @return
		 */
		private int averageShrink(int[] rgb, int width_div, int height_div, int pos_x, int pos_y, int image_width, int image_height) {
			int sum = 0;
			for ( int x = pos_x*width_div; x < pos_x*width_div + width_div; x++ ) {
				for ( int y = pos_y*image_width*height_div; y < pos_y*image_width*height_div + image_width*height_div; y+= image_width ) {
					Color c = new Color(rgb[x+y]);
					sum += c.getRed();
				}
			}
			
			int temp = sum/(width_div*height_div);
			Color c = new Color(temp,temp,temp);

			return c.getRGB();
		}
		
	}
	
	public class HorzHistListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			histogram("horz");
		}
	}
	
	public class VertHistListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			histogram("vert");
		}
	}
	
	public class BothHistListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			histogram("both");
		}
	}
}
