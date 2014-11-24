package stal8920;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageManip extends JPanel {

	public boolean USED = true;
	ImageArea myArea;
	BufferedImage myPic = null;
	Histogram histogram;

	public ImageManip(ImageArea area, BufferedImage myImage){
		myArea = area;
		myPic = myImage;
		drawComponents();
		addListeners();
	}

	JButton lowPass = new JButton("Apply Low Pass Filter");
	JButton highPass = new JButton("Apply High Pass Filter");
	JButton scale = new JButton("Scale The Image");
	JButton thin = new JButton("Thin The Image (ZS)");
	JButton feature = new JButton("Create feature vector");
	JButton NAO = new JButton("The Nao Button");

	public void drawComponents(){
		JPanel pnl = new JPanel();
		pnl.setLayout(new GridLayout(8, 1));
		pnl.add(lowPass);
		pnl.add(highPass);
		pnl.add(scale);
		pnl.add(thin);
		pnl.add(feature);
		pnl.add(NAO);
		this.add(pnl);
	}

	public void lowPass(BufferedImage myPic, int width, int height){

		//The empty image we manipulate
		BufferedImage tempImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);

		//The old image loaded in
		BufferedImage originalImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g1 = originalImage.getGraphics(); 
		g1.drawImage(myPic, 0, 0, null);  
		g1.dispose();

		//Looping through the pixels
		for(int i = 2; i <=width-2; i++){
			for(int j = 2; j <= height-2; j++){

				//From old image to new image
				int temp_pixel = lowPassUtil(i,j,originalImage);
				Color c_temp = new Color(temp_pixel, temp_pixel, temp_pixel);
				tempImage.setRGB(i, j, c_temp.getRGB());
			}
		}

		//Loading in the filtered image
		Graphics g2 = tempImage.getGraphics(); 
		g2.drawImage(tempImage, 0, 0, null);  
		g2.dispose();

		//Displaying
		JLabel picLabel2 = new JLabel(new ImageIcon(tempImage));
		myArea.add(picLabel2);
		myArea.validate();
	}

	public void highPass(BufferedImage myPic, int width, int height){

		//The empty image we manipulate
		BufferedImage tempImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);

		//The old image loaded in
		BufferedImage originalImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g1 = originalImage.getGraphics(); 
		g1.drawImage(myPic, 0, 0, null);  
		g1.dispose();


		//Looping through the pixels
		for(int i = 2; i <=width-2; i++){
			for(int j = 2; j <= height-2; j++){

				//From old image to new image
				int temp_pixel = highPassUtil(i,j,originalImage);
				Color c_temp = new Color(temp_pixel, temp_pixel, temp_pixel);
				tempImage.setRGB(i, j, c_temp.getRGB());
			}
		}

		//Loading in the filtered image
		Graphics g2 = tempImage.getGraphics(); 
		g2.drawImage(tempImage, 0, 0, null);  
		g2.dispose();

		//Displaying
		JLabel picLabel2 = new JLabel(new ImageIcon(tempImage));
		myArea.add(picLabel2);
		myArea.validate();
	}

	public void scale(BufferedImage myPic, int width, int height){

		//The empty image we manipulate
		BufferedImage tempImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);

		//The old image loaded in
		BufferedImage originalImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g1 = originalImage.getGraphics(); 
		g1.drawImage(myPic, 0, 0, null);  
		g1.dispose();

		int xpos = 2;
		int ypos = 2;

		//Looping through the pixels
		for(int i = 2; i <=width-2; i+=3){

			//reset
			ypos = 2;

			for(int j = 2; j <= height-2; j+=3){

				//From old image to new image
				int temp_pixel = scaleUtil(i,j,originalImage);
				Color c_temp = new Color(temp_pixel, temp_pixel, temp_pixel);
				tempImage.setRGB(xpos, ypos, c_temp.getRGB());

				ypos++;
			}
			xpos++;
		}

		//Loading in the filtered image
		Graphics g2 = tempImage.getGraphics(); 
		g2.drawImage(tempImage, 0, 0, null);  
		g2.dispose();

		//Displaying
		JLabel picLabel2 = new JLabel(new ImageIcon(tempImage));
		myArea.add(picLabel2);
		myArea.validate();
	}

	public void thin(BufferedImage myPic, int width, int height){

		//The empty images we manipulate
		BufferedImage midImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		BufferedImage finalImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);

		//The old image loaded in
		BufferedImage originalImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g1 = originalImage.getGraphics(); 
		g1.drawImage(myPic, 0, 0, null);  
		g1.dispose();
		while(USED == true){
			USED = false;
			//Looping through the pixels
			for(int i = 2; i <=width-2; i++){
				for(int j = 2; j <= height-2; j++){

					//From old image to new image
					int[] tempArray = thinUtil(i,j,originalImage);
					int temp_pixel = ZSsubPass(tempArray, true); //first sub iteration
					Color c_temp = new Color(temp_pixel, temp_pixel, temp_pixel);
					midImage.setRGB(i, j, c_temp.getRGB());
				}
			}

			//Looping through the pixels
			for(int i = 2; i <=width-2; i++){
				for(int j = 2; j <= height-2; j++){

					//From old image to new image
					int[] tempArray = thinUtil(i,j,midImage);
					int temp_pixel = ZSsubPass(tempArray, false); //second sub iteration
					Color c_temp = new Color(temp_pixel, temp_pixel, temp_pixel);
					finalImage.setRGB(i, j, c_temp.getRGB());
				}
			}
			originalImage = finalImage;

		}
		//Loading in the filtered image
		Graphics g3 = finalImage.getGraphics(); 
		g3.drawImage(finalImage, 0, 0, null);  
		g3.dispose();

		//Displaying
		JLabel picLabel3 = new JLabel(new ImageIcon(finalImage));
		myArea.add(picLabel3);
		myArea.validate();
	}

	public void feature(BufferedImage myPic, int width, int height){

		//The image loaded in
		BufferedImage originalImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g1 = originalImage.getGraphics(); 
		g1.drawImage(myPic, 0, 0, null);  
		g1.dispose();

		int xStep = width/2; //can modify here to /x
		int yStep = height/2; //and here

		float [][] vector1 = new float[2][2];
		float [] vector2 = new float[4];

		for (int i = 0; i < 2; i++){
			for (int j =0; j < 2; j++){
				vector1[i][j] = featureUtil(xStep*i, yStep*j, xStep, yStep, originalImage);
			}
		}

		//print the array
		for(int i = 0;i<=1;i++){
			System.out.format("%.3f  %.3f", vector1[i][0], vector1[i][1] );
			System.out.println(" ");
		}

		featureUtilPie( originalImage, vector2);

		System.out.format("\n%.3f North\n%.3f South\n%.3f West\n%.3f East\n", vector2[0], vector2[1], vector2[2], vector2[3] );
	}

	public void NAOPass(BufferedImage myPic, int width, int height){

		//The old image loaded in
		BufferedImage originalImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g1 = originalImage.getGraphics(); 
		g1.drawImage(myPic, 0, 0, null);  
		g1.dispose();		

		BufferedImage rotate = originalImage;

		//filters
		int[] array1 = {1, 8, 1, 0, 0, 0, -1, -8, -1};
		int[] array2 = {-1, 0, 1, -8, 0, 8, -1, 0, 1};
		int[] array3 = {1,4,1,4,-19,4,1,4,1};//tester

		BufferedImage vertImage = FilterImage.filterImage(originalImage, array1);
		histogram = new Histogram("vert", vertImage);

		float vertMax = maxOfHist(Histogram.histVert(vertImage.getWidth(),vertImage.getHeight(),vertImage));
		if (vertMax <  25.0){
			float rotateFactor = (30 - vertMax)/80;
			rotate = rotateByX(originalImage,rotateFactor);
		}

		int yMid = skipToBlack( histogram.getImage(), histogram.getImage().getHeight(), 15, "V");
		int[] yArray = setParams(yMid,histogram.getImage().getHeight(),30);//pixel allowance
		if(yArray[0] == 0){ yArray[1] = 60; }
		BufferedImage numStrip = new BufferedImage(width, 62, BufferedImage.TYPE_BYTE_GRAY);
		for(int x = 0; x < width; x++){
			int yNew = yArray[0];
			for(int y = 0; y < (yArray[1]-yArray[0]); y++){
				Color c= new Color(rotate.getRGB(x, yNew), true);//inputs here
				numStrip.setRGB(x, y, c.getRGB());
				yNew ++;
			}
		}

		BufferedImage horzImage = FilterImage.filterImage(numStrip, array2);
		histogram = new Histogram("horz", horzImage);
		int xMid = skipToBlack( histogram.getImage(), histogram.getImage().getWidth(), 1, "H");
		
		int xBound = (int)(histogram.getImage().getWidth()/2);//pixel allowance
		int xPos = 0;
		int[] xArray = setParams(xMid,histogram.getImage().getWidth(),xBound/2);
		BufferedImage nums = new BufferedImage(xBound, 60, BufferedImage.TYPE_BYTE_GRAY);
		for(int x = xArray[0]; x < xArray[1]; x++){
			for(int y = 0; y < 60; y++){
				Color c= new Color(numStrip.getRGB(x, y), true);
				nums.setRGB(xPos, y, c.getRGB());
			}
			xPos++;
		}
		displayImage(nums);

	}
	
	public int[] setParams(int mid, int max, int bound){
		int[] retArray = new int[2];
		if( mid -bound < 0){retArray[0] = 0;} 
		else{retArray[0] = mid-bound;}
		if(mid + bound >  max){ retArray[1] =  max;}
		else{ retArray[1] = mid+bound;}
		return retArray;
	}
	
	public BufferedImage rotateByX(BufferedImage from, float by){
		AffineTransform transform = new AffineTransform();
		transform.rotate(by, from.getWidth()/2, from.getHeight()/2);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);//Image gets slightly darker here
		BufferedImage to = op.filter(from, null);
		return to;
	}

	public float maxOfHist(float[] myArray){
		Float[] Array = new Float[myArray.length];
		int alpha = 0;
		for (float value : myArray) {
			Array[alpha++] = Float.valueOf(value);
		}
		Arrays.sort(myArray);
		float max = myArray[myArray.length-1];
		//int location = Arrays.asList(intArray).indexOf(max);
		return max;
	}

	public void displayImage(BufferedImage image){
		//Loading in the filtered image
		Graphics g2 = image.getGraphics(); 
		g2.drawImage(image, 0, 0, null);  
		g2.dispose();

		//Displaying
		JLabel picLabel2 = new JLabel(new ImageIcon(image));
		myArea.add(picLabel2);
		myArea.validate();
	}


	//skips to first large run of black on histogram
	public int skipToBlack(BufferedImage image, int max, int thresh, String dir){
		int sum = 0;
		int count = 0;
		int i = 0;
		int retVal = 0;
		boolean isBlack = false;

		if (dir == "H"){
			while( i < max -1){
				if(new Color(image.getRGB(i, thresh)).getRed() == 0){ //THRESHOLD
					sum+=i;
					count++;
				} 
				i++;
			}
			if( count != 0){
				retVal = sum/count;
			}
		}

		//vertical histogram
		else if(dir =="V"){
			while (isBlack == false && i < max - 1){ 
				if( new Color(image.getRGB(thresh, i)).getRed() == 0){ //THRESHOLD
					isBlack = true;
				} 
				i++;
			}
			while (isBlack == true && i < max - 1){
				sum += i;
				count ++;
				if(new Color(image.getRGB(thresh, i)).getRed() != 0){ //THRESHOLD 
					isBlack = false;
				}
				i++;
			}
			if( count != 0){
				retVal = sum/count;
			}
		}
		if(i == max){
			System.out.println("Not Found");
		}
		return retVal;
	}

	//Add button listeners
	public void addListeners() {
		lowPass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				lowPass(myPic, myPic.getWidth(), myPic.getHeight());
			}
		});

		highPass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				highPass(myPic, myPic.getWidth(), myPic.getHeight());
			}
		});

		scale.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				scale(myPic, myPic.getWidth(), myPic.getHeight());
			}
		});

		thin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				thin(myPic, myPic.getWidth(), myPic.getHeight());
			}
		});

		feature.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				feature(myPic, myPic.getWidth(), myPic.getHeight());
			}
		});

		NAO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				NAOPass(myPic, myPic.getWidth(), myPic.getHeight());
			}
		});
	}

	public int lowPassUtil(int x, int y, BufferedImage image){
		int sum = 0;
		for(int i =-1;i<=1;i++){
			for (int j = -1; j <= 1; j++){
				Color c= new Color(image.getRGB(x+i, y+j), true);
				sum+=c.getRed();
			}
		}
		sum = sum/9;
		return sum;
	}

	public int highPassUtil(int x, int y, BufferedImage image){
		int sum = 0;
		for(int i =-1;i<=1;i++){
			for (int j = -1; j <= 1; j++){
				Color c= new Color(image.getRGB(x+i, y+j), true);
				int temp = -1*c.getRed();
				if(i ==0 && j ==0){
					temp = temp*-9;
				}
				sum+=temp;
			}
		}
		if(sum > 255){
			sum = 255;
		}
		else if (sum < 0){
			sum =0;
		}
		return sum;
	}

	public int scaleUtil(int x, int y, BufferedImage image){
		int[] myArray = new int[9];
		int index = 0;
		for(int i =-1;i<=1;i++){
			for (int j = -1; j <= 1; j++){
				Color c= new Color(image.getRGB(x+i, y+j), true);
				myArray[index] = c.getRed();
				index++;
			}
		}

		Arrays.sort(myArray);
		int median = myArray[4];
		return median;
	}

	public int[] thinUtil(int x, int y, BufferedImage image){
		int[] myArray = new int[9];
		int index = 0;
		for(int i =-1;i<=1;i++){
			for (int j = -1; j <= 1; j++){
				Color c= new Color(image.getRGB(x+i, y+j), true);
				if (c.getRed() >= 127){
					myArray[index] = 0;
				}
				else{
					myArray[index] = 1;
				}
				index++;
			}
		}

		//Manually rearranging the array
		int[] thinArray = new int[9];
		thinArray[0] = myArray[0]; //NW pixel
		thinArray[1] = myArray[1]; //N pixel
		thinArray[2] = myArray[2]; //NE pixel
		thinArray[3] = myArray[5]; //E pixel
		thinArray[4] = myArray[8]; //SE pixel
		thinArray[5] = myArray[7]; //S pixel
		thinArray[6] = myArray[6]; //SW pixel
		thinArray[7] = myArray[3]; //W pixel
		thinArray[8] = myArray[4]; //Center pixel

		return thinArray;
	}

	public int ZSsubPass(int[] thinArray, boolean side){
		int sumPixels = 0;
		for (int i = 0; i < 8; i++){
			sumPixels += thinArray[i]; //only up to 8 because the 9th array slot is the center pixel
		}

		//Condition (a)
		if(sumPixels >= 2 && sumPixels <= 6){

			int sumCycles = 0;
			for(int j = 0; j < 7;j++){
				if(thinArray[j] == 0 && thinArray[j+1] ==1){ sumCycles ++;}
			}
			if (thinArray[7] == 0 && thinArray[0] == 1){ sumCycles ++;}

			//Condition (b)
			if(sumCycles == 1 && thinArray[8] != 0){

				//System.out.println(Arrays.toString(thinArray));
				if (side == true){

					//Conditions (c)and (d)	
					if(thinArray[1]*thinArray[3]*thinArray[5] == 0 && thinArray[3]*thinArray[5]*thinArray[7] == 0){ thinArray[8] = 0; USED = true; }
				}
				else{

					//Conditions (c') and (d')	
					if(thinArray[1]*thinArray[3]*thinArray[7] == 0 && thinArray[1]*thinArray[5]*thinArray[7] == 0){ thinArray[8] = 0; USED = true;}
				}

			}
		}

		//Assigning black or white pixel
		int result = 0; //i.e. black
		if (thinArray[8] == 0){
			result = 255;
		}
		return result;
	}

	public float featureUtil(int x, int y, int xStep, int yStep, BufferedImage image){
		int blackSum = 0;
		int whiteSum = 0;
		for(int i = x; i < x+xStep; i++){
			for (int j = y; j < y+yStep; j++){
				Color c= new Color(image.getRGB(i, j), true);
				if( c.getRed() == 255){ whiteSum++; }
				if( c.getRed() == 0){ blackSum++; }
			}
		}
		float d = whiteSum + blackSum;
		float sum = blackSum/d;
		return sum;
	}

	public void featureUtilPie(BufferedImage image, float[] vector){
		int height = image.getHeight();
		int width = image.getWidth();

		int blackSum = 0;
		int whiteSum = 0;
		int mid = height/2;


		//North pass
		for( int y = 0; y < mid; y++ ){
			for (int x = y; x < width - y; x ++){
				Color c= new Color(image.getRGB(x, y), true);
				if( c.getRed() == 255){ whiteSum++; }
				if( c.getRed() == 0){ blackSum++; }
			}
		}

		float sumTotal = whiteSum + blackSum;
		vector[0] = blackSum/sumTotal;

		blackSum = 0;
		whiteSum = 0;
		int indent = 0;

		//South pass
		for( int y = height -1; y > mid; y-- ){
			for (int x = indent; x < width - indent; x ++){
				Color c= new Color(image.getRGB(x, y), true);
				if( c.getRed() == 255){ whiteSum++; }
				if( c.getRed() == 0){ blackSum++; }
			}
			indent++;
		}

		sumTotal = whiteSum + blackSum;
		vector[1] = blackSum/sumTotal;

		blackSum = 0;
		whiteSum = 0;

		//West pass
		for( int y = 0; y < mid; y++ ){
			for (int x = y; x < width - y; x ++){
				Color c= new Color(image.getRGB(y, x), true);
				if( c.getRed() == 255){ whiteSum++; }
				if( c.getRed() == 0){ blackSum++; }
			}
		}

		sumTotal = whiteSum + blackSum;
		vector[2] = blackSum/sumTotal;

		blackSum = 0;
		whiteSum = 0;
		indent = 0;

		//East pass
		for( int y = height -1; y > mid; y-- ){
			for (int x = indent; x < width - indent; x ++){
				Color c= new Color(image.getRGB(x, y), true);
				if( c.getRed() == 255){ whiteSum++; }
				if( c.getRed() == 0){ blackSum++; }
				//System.out.println( x + " " + y + " " + c.getRed());
			}
			indent++;
		}

		sumTotal = whiteSum + blackSum;
		vector[3] = blackSum/sumTotal;
	}

}
