
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.*;


public class FindPlaque {

	private ArrayList<Color> values = new ArrayList<>();
	private ArrayList<Integer> counts = new ArrayList<>();
	public ArrayList<int[]> positions = new ArrayList<>();	//{x,y}
	
	/*public static void main(String[] args) {
		try {
			new FindPlaque(ImageIO.read(new File("image1.png") ));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}*/ 
	
	public FindPlaque(BufferedImage image) {
		printFoundColors(image.getRGB(0,0, image.getWidth(), image.getHeight(), null, 0, image.getWidth() ), image.getWidth() );
		//printCounts();
	}
	
	private void printFoundColors(int image_data[], int width) {
		for ( int i = 0; i < image_data.length; i++ ) {
			Color c = new Color(image_data[i]);
			for ( int j = 0; j < PlaqueDB.COLORS.length; j++ ) {
				if ( c.equals( PlaqueDB.COLORS[j] ) ) {
					if ( values.size() == 0 ) {
						values.add(c);
						counts.add(1);
					} else {
						int y = i / width;
						int x = i % width;
						int pos[] = {x,y};
						positions.add(pos);
						//System.out.println("pos = (" + x + ", " + y + ")");
						countSameData(c);
					}
				}
			}
		}
		findCenter(positions);
	}
	
	private void countSameData( Color c ) {
		boolean bln = false;
		for ( int j = 0; j < values.size(); j++ ) {
			if ( c.equals(this.values.get(j) ) ) {
				this.counts.set(j, this.counts.get(j)+1);
				bln = true;
				break;
			}
		}
		
		if ( bln == false ) {
			this.values.add(c);
			this.counts.add(1);
		}
	}
	
	public void printCounts() {
		for ( int i = 0; i < this.values.size(); i++ ) {
			Color c = this.values.get(i);
			//if ( this.counts.get(i) > 100 ) {
				System.out.println("("+c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")" +  this.counts.get(i) );
			//}
		}
	}
	
	private void findCenter(ArrayList<int[]> array) {
		int sum_x = 0, sum_y = 0;
		for ( int i = 0; i < array.size(); i++ ) {
			sum_x += array.get(i)[0];
			sum_y += array.get(i)[1];
		}
		int value[] = {sum_x/array.size(),sum_y/array.size()};
		System.out.println("Center point of plaque is (" + value[0] + "," + value[1] + ")");
	}
}