
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.*;

public class PrintColors {

	private ArrayList<Color> values = new ArrayList<>();
	private ArrayList<Integer> counts = new ArrayList<>();

	public static void main(String[] args) {
		new PrintColors();
	} 
	
	public PrintColors() {
		System.out.println("Reading Images");
		BufferedImage images[] = readImages();
		System.out.println("Counting Colors");
		for ( int i = 0; i < images.length; i++ ) {
			System.out.println("On Image: " + i );
			countColors(images[i].getRGB(0,0, images[i].getWidth(), images[i].getHeight(), null, 0, images[i].getWidth() ) );
		}
		System.out.println("Printing Colors");
		printCounts();
	}
	
	public BufferedImage[] readImages() {
		BufferedImage images[] = new BufferedImage[5];
		for ( int i = 1; i < 6; i++ ) {
			try {
				images[i-1] = ImageIO.read(new File("images/p" + i + ".png") );
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return images;
	}
	
	public void countColors(int image_data[]) {
		
		this.values.add(new Color(image_data[0] ) );
		this.counts.add(1);
		for ( int i = 1; i < image_data.length; i++ ) {
			Color c = new Color(image_data[i]);
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
	}
	
	public void printCounts() {
		int count = 0;
		for ( int i = 0; i < this.values.size(); i++ ) {
			Color c = this.values.get(i);
			if ( c.getBlue() > 100 && this.counts.get(i) > 10 &&
				c.getBlue() < 200 && c.getRed() < 200 && c.getBlue() < 200	
				) {		//this.counts.get(i) > 100 ) {
				System.out.println("new Color(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")," );
				count++;
			}
		}
		System.out.println("Number of colors: " + count );
	}

}