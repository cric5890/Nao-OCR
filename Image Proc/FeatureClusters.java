import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/*
	 * FeatureClusters
	 * - Stores the cluster centers for feature vectors of characters 0-9
	 * - Allows you to supply a feature vector and it will return the cluster it probably belongs to
	 */
	
	public class FeatureClusters{
		private final int FEATURE_LENGTH = 8;
		private float[][] cluster_centers = new float[10][FEATURE_LENGTH];
		
		
		public static void main(String[] args){
			FeatureClusters clusters = new FeatureClusters();
			clusters.loadClusters();
			clusters.printCenters();
		}
		
		public FeatureClusters(){	
		}
		
		public void TEST(int iterations){
			float feature0[] = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
			initClusterCenter(0,feature0);
			float feature1[] = {1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f};
			initClusterCenter(1,feature1);
			float feature2[] = {2.0f,2.0f,2.0f,2.0f,2.0f,2.0f,2.0f,2.0f};
			initClusterCenter(2,feature2);
			float feature3[] = {3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f};
			initClusterCenter(3,feature3);
			float feature4[] = {4.0f,4.0f,4.0f,4.0f,4.0f,4.0f,4.0f,4.0f};
			initClusterCenter(4,feature4);
			float feature5[] = {5.0f,5.0f,5.0f,5.0f,5.0f,5.0f,5.0f,5.0f};
			initClusterCenter(5,feature5);
			float feature6[] = {6.0f,6.0f,6.0f,6.0f,6.0f,6.0f,6.0f,6.0f};
			initClusterCenter(6,feature6);
			float feature7[] = {7.0f,7.0f,7.0f,7.0f,7.0f,7.0f,7.0f,7.0f};
			initClusterCenter(7,feature7);
			float feature8[] = {8.0f,8.0f,8.0f,8.0f,8.0f,8.0f,8.0f,8.0f};
			initClusterCenter(8,feature8);
			float feature9[] = {9.0f,9.0f,9.0f,9.0f,9.0f,9.0f,9.0f,9.0f};
			initClusterCenter(9,feature9);
			
			
			float feature[] = new float[FEATURE_LENGTH];
			int closest;
			
			for(int i=0; i<iterations; i++){
				for(int j=0; j<FEATURE_LENGTH; j++){
					feature[j] = (float)Math.random()*9;
				}
				
				closest = getClosestCenter(feature);
				System.out.println(	"Feature: [" + feature[0] +
									"," + feature[1] +
									"," + feature[2] +
									"," + feature[3] +
									"," + feature[4] +
									"," + feature[5] +
									"," + feature[6] +
									"," + feature[7] +
									"] is closest to " + closest);
			}
		}
		
		/*
		 *  initClusterCenter
		 *  - sets a given feature vector as the cluster center for given number
		 */
		public void initClusterCenter(int number, float[] feature){
			if(feature.length == FEATURE_LENGTH){
				cluster_centers[number] = feature;
			}else{
				System.err.println("Invalid feature: length does not match FEATURE_LENGTH");
			}
		}
		
		/*
		 * includeFeature
		 * - uses a given feature to adjust the cluster center for a given number
		 */
		public void includeFeature(int number, float[] feature){
			if(feature.length == FEATURE_LENGTH){
				if(cluster_centers[number] != null){
					float center[] = cluster_centers[number];
					cluster_centers[number][0] = (center[0] + feature[0]) /2;
					cluster_centers[number][1] = (center[1] + feature[1]) /2;
					cluster_centers[number][2] = (center[2] + feature[2]) /2;
					cluster_centers[number][3] = (center[3] + feature[3]) /2;
					cluster_centers[number][4] = (center[4] + feature[4]) /2;
					cluster_centers[number][5] = (center[5] + feature[5]) /2;
					cluster_centers[number][6] = (center[6] + feature[6]) /2;
					cluster_centers[number][7] = (center[7] + feature[7]) /2;
				}else{
					System.err.println("Cluster center not defined");
				}
			}else{
				System.err.println("Invalid feature: length does not match FEATURE_LENGTH");
			}
		}
		
		
		/* 
		 * getClosestCenter
		 * - determines the cluster center that is closest to a given feature using euclidian distance.
		 * - returns -1 if error
		 */
		public int getClosestCenter(float[] feature){
			int closest = -1;
			
			float distances[] = new float[10];
			
			if(feature.length == FEATURE_LENGTH){
				float dist, minDist;
				float center[] = null;
				center = cluster_centers[0];
				dist = (float) Math.sqrt(	Math.pow(feature[0]-center[0],2)+
											Math.pow(feature[1]-center[1],2)+
											Math.pow(feature[2]-center[2],2)+
											Math.pow(feature[3]-center[3],2)+
											Math.pow(feature[4]-center[4],2)+
											Math.pow(feature[5]-center[5],2)+
											Math.pow(feature[6]-center[6],2)+
											Math.pow(feature[7]-center[7],2));
				minDist = dist;
				closest = 0;
				distances[0] = dist;
				for(int i=1; i<cluster_centers.length; i++){
					center = cluster_centers[i];
					dist = (float) Math.sqrt(	Math.pow(feature[0]-center[0],2)+
												Math.pow(feature[1]-center[1],2)+
												Math.pow(feature[2]-center[2],2)+
												Math.pow(feature[3]-center[3],2)+
												Math.pow(feature[4]-center[4],2)+
												Math.pow(feature[5]-center[5],2)+
												Math.pow(feature[6]-center[6],2)+
												Math.pow(feature[7]-center[7],2));
					distances[i] = dist;
					if(dist < minDist){
						minDist = dist;
						closest = i;
					}
				}
				
				if(minDist > 0.20){
					closest = -1;
				}
				
				System.out.println("["+distances[0]+","+distances[1]+","+distances[2]+","+distances[3]+","+distances[4]+","+distances[5]+","+distances[6]+","+distances[7]+","+distances[8]+","+distances[9]+"]");
				
			}else{
				System.out.println("Invalid feature: length does not math FEATURE_LENGTH");
			}
			return closest;
		}
		
		public void saveClusters(){
			try{
			
				File f = new File("c:/centers.csv");
				
				if(!f.exists()){
					f.createNewFile();
				}
				
				FileWriter fw = new FileWriter(f.getAbsolutePath());
				BufferedWriter w = new BufferedWriter(fw);
				
				float center[] = null;
				String csv = null;
				
				for(int i=0; i < cluster_centers.length; i++){
					center = cluster_centers[i];
					csv = 	center[0]+","
							+center[1]+","
							+center[2]+","
							+center[3]+","
							+center[4]+","
							+center[5]+","
							+center[6]+","
							+center[7]+"\n";
					w.write(csv);
				}
				
				w.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void loadClusters(){
			try{
				BufferedReader r = new BufferedReader(new FileReader("c:/centers.csv"));
				
				float center[] = null;
				
				String line = r.readLine();
				String csv[] = null;
				
				
				int index = 0;
				
				while(line != null && index < 10){
					csv = line.split(",");
					if(csv.length == FEATURE_LENGTH){
						center = new float[8];
						center[0] = Float.parseFloat(csv[0]);
						center[1] = Float.parseFloat(csv[1]);
						center[2] = Float.parseFloat(csv[2]);
						center[3] = Float.parseFloat(csv[3]);
						center[4] = Float.parseFloat(csv[4]);
						center[5] = Float.parseFloat(csv[5]);
						center[6] = Float.parseFloat(csv[6]);
						center[7] = Float.parseFloat(csv[7]);
						
						initClusterCenter(index,center);
						line = r.readLine();
						index++;
					}else{
						//error
					}
					
				}
				
				r.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		public float[] extractFeature(BufferedImage img){
			float width, height,area;
			float pos_slope, neg_slope; 
			
			float feature[] = new float[8];
			
			height = img.getHeight();
			width = img.getWidth();
			area = width*height;
			
			
			
			pos_slope = height/width;
			neg_slope = -height/width;
			
			//System.out.println("H: "+height+" W: "+width+" A: "+area+" PS: "+pos_slope+" NS: "+neg_slope);
			
			Color levels = null;
			
			for(int x=0; x < width; x++){
				for(int y=0; y < height; y++){
					levels = new Color(img.getRGB(x, y));
					if(levels.getRed() < 10){
						//Horz/Vert Slicing
						if(x <= width/2 && y <= height/2){
							//first sector
							feature[0]++;
						}else if(x <= width/2 && y > height/2){
							//second sector
							feature[1]++;
						}else if(x > width/2 && y <= height/2){
							//third sector
							feature[2]++;
						}else if(x > width/2 && y > height/2){
							//fourth sector
							feature[3]++;
						}
						
						//Diagonal Slicing
						if(-y > (neg_slope*x) && -y > ((pos_slope*x) - height)){
							//first sector
							feature[4]++;
						}else if(-y > (neg_slope*x) && -y <= ((pos_slope*x) - height)){
							//second sector
							feature[5]++;
						}else if(-y <= (neg_slope*x) && -y <= ((pos_slope*x) - height)){
							//third sector
							feature[6]++;
						}else if(-y <= (neg_slope*x) && -y > ((pos_slope*x) - height)){
							//fourth sector
							feature[7]++;
						}
					}
				}
			}
			
			feature[0] = feature[0]/(area/4);
			feature[1] = feature[1]/(area/4);
			feature[2] = feature[2]/(area/4);
			feature[3] = feature[3]/(area/4);
			feature[4] = feature[4]/(area/4);
			feature[5] = feature[5]/(area/4);
			feature[6] = feature[6]/(area/4);
			feature[7] = feature[7]/(area/4);
			
			return feature;
			
		}
		
		
		public void printCenters(){
			float center[] = null;
			for(int i=0; i<cluster_centers.length; i++){
				center = cluster_centers[i];
				System.out.println("["+center[0]+","+center[1]+","+center[2]+","+center[3]+","+center[4]+","+center[5]+","+center[6]+","+center[7]+"]");
			}
		}
		
	}