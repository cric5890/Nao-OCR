/*
 * Main.cpp
 *
 *  Created on: Oct 17, 2014
 *      Author: Wesley Crick
 */

#include <GL/glew.h>
#include <GL/gl.h>
 #include <GL/glut.h>


 #include <png.h>
 #include <cstdio>
 #include <string>
#include <fstream>
#include <iostream>
#include <winsock.h>

#include <stdlib.h>
 #include <math.h>
 #include <time.h>

 #define TEXTURE_LOAD_ERROR 0

 using namespace std;

GLuint texture[2];
GLint width, height;
GLuint program;

 bool shader_on = true;

void initTexture(int num_of_textures) {
	glGenTextures(num_of_textures, texture);
}

 void generateTexture(int width, int height, char * image_data, int index ) {
	//Now generate the OpenGL texture object
	//GLuint texture;
	 if ( index == 0 ) {
		 glActiveTexture(GL_TEXTURE0);
	 } else {
		 glActiveTexture(GL_TEXTURE1);
	 }
	glBindTexture(GL_TEXTURE_2D, texture[index]);
	glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, width, height, 0,
			GL_RGB, GL_UNSIGNED_BYTE, (GLvoid*) image_data);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

	//return texture;
 }

 /** loadTexture
  * 	loads a png file into an opengl texture object, using cstdio , libpng, and opengl.
  *
  * 	\param filename : the png file to be loaded
  * 	\param width : width of png, to be updated as a side effect of this function
  * 	\param height : height of png, to be updated as a side effect of this function
  *
  * 	\return GLuint : an opengl texture id.  Will be 0 if there is a major error,
  * 					should be validated by the client of this function.
  *
  */
 void loadTexture(const string filename, int width, int height)
 {
   //header for testing if it is a png
   png_byte header[8];

   //open file as binary
   FILE *fp = fopen(filename.c_str(), "rb");
   if (!fp) {
     return;// TEXTURE_LOAD_ERROR;
   }

   //read the header
   fread(header, 1, 8, fp);

   //test if png
   int is_png = !png_sig_cmp(header, 0, 8);
   if (!is_png) {
     fclose(fp);
     return;// TEXTURE_LOAD_ERROR;
   }

   //create png struct
   png_structp png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL,
       NULL, NULL);
   if (!png_ptr) {
     fclose(fp);
     return;// (TEXTURE_LOAD_ERROR);
   }

   //create png info struct
   png_infop info_ptr = png_create_info_struct(png_ptr);
   if (!info_ptr) {
     png_destroy_read_struct(&png_ptr, (png_infopp) NULL, (png_infopp) NULL);
     fclose(fp);
     return;// (TEXTURE_LOAD_ERROR);
   }

   //create png info struct
   png_infop end_info = png_create_info_struct(png_ptr);
   if (!end_info) {
     png_destroy_read_struct(&png_ptr, &info_ptr, (png_infopp) NULL);
     fclose(fp);
     return;// (TEXTURE_LOAD_ERROR);
   }

   //png error stuff, not sure libpng man suggests this.
   if (setjmp(png_jmpbuf(png_ptr))) {
     png_destroy_read_struct(&png_ptr, &info_ptr, &end_info);
     fclose(fp);
     return;// (TEXTURE_LOAD_ERROR);
   }

   //init png reading
   png_init_io(png_ptr, fp);

   //let libpng know you already read the first 8 bytes
   png_set_sig_bytes(png_ptr, 8);

   // read all the info up to the image data
   png_read_info(png_ptr, info_ptr);

   //variables to pass to get info
   int bit_depth, color_type;
   png_uint_32 twidth, theight;

   // get info about png
   png_get_IHDR(png_ptr, info_ptr, &twidth, &theight, &bit_depth, &color_type,
       NULL, NULL, NULL);

   //update width and height based on png info
   width = twidth;
   height = theight;

   // Update the png info struct.
   png_read_update_info(png_ptr, info_ptr);

   // Row size in bytes.
   int rowbytes = png_get_rowbytes(png_ptr, info_ptr);

   // Allocate the image_data as a big block, to be given to opengl
   png_byte *image_data = new png_byte[rowbytes * height];
   if (!image_data) {
     //clean up memory and close stuff
     png_destroy_read_struct(&png_ptr, &info_ptr, &end_info);
     fclose(fp);
     return;// TEXTURE_LOAD_ERROR;
   }

   //row_pointers is for pointing to image_data for reading the png with libpng
   png_bytep *row_pointers = new png_bytep[height];
   if (!row_pointers) {
     //clean up memory and close stuff
     png_destroy_read_struct(&png_ptr, &info_ptr, &end_info);
     delete[] image_data;
     fclose(fp);
     return;// TEXTURE_LOAD_ERROR;
   }
   // set the individual row_pointers to point at the correct offsets of image_data
   for (int i = 0; i < height; ++i)
     row_pointers[height - 1 - i] = image_data + i * rowbytes;

   //read the png into image_data through row_pointers
   png_read_image(png_ptr, row_pointers);

   //Now generate the OpenGL texture object
   generateTexture(width, height, (char *)image_data,0);
   /*glGenTextures(1, &texture);
   glBindTexture(GL_TEXTURE_2D, texture);
   glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, width, height, 0,
       GL_RGB, GL_UNSIGNED_BYTE, (GLvoid*) image_data);
   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
*/
   //clean up memory and close stuff
   png_destroy_read_struct(&png_ptr, &info_ptr, &end_info);
   delete[] image_data;
   delete[] row_pointers;
   fclose(fp);

  // return texture;
 }



 void render()
 {
	 clock_t start = clock();
     glMatrixMode(GL_PROJECTION);
     glLoadIdentity();
     /* fov, aspect, near, far */
     //60    1       1      10
     gluPerspective(60, 1, 1, 10);
     gluLookAt(0, 0, -2, /* eye */
               0, 0, 2, /* center */		//0,0,2
               0, 1, 0); /* up */
     glMatrixMode(GL_MODELVIEW);
     glLoadIdentity();

     glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
     glPushAttrib(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
         glEnable(GL_TEXTURE_2D);

         /* create a square on the XY
            note that OpenGL origin is at the lower left
            but texture origin is at upper left
            => it has to be mirrored
            (gasman knows why i have to mirror X as well) */
         glBegin(GL_QUADS);
			 glNormal3f(0.0, 0.0, 1.0);
			 glTexCoord2d(1, 0); glVertex3f(0.0 - 1, 0.0 - 1, 0.0);		//1,1
			 glTexCoord2d(1, 1); glVertex3f(0.0 - 1, 2.0 - 1, 0.0);		//1,0
			 glTexCoord2d(0, 1); glVertex3f(2.0 - 1, 2.0 - 1, 0.0);		//0,0
			 glTexCoord2d(0, 0); glVertex3f(2.0 - 1, 0.0 - 1, 0.0);		//0,1
         glEnd();

         glDisable(GL_TEXTURE_2D);
     glPopAttrib();

     glFlush();

     glutSwapBuffers();
     float end = (float)((float)clock() - (float)start)/CLOCKS_PER_SEC;
     printf("%f clicks\n", end);
 }

 void initShaders() {
	 //read in shader files
	 std::ifstream v_file("shader.vert");
	 std::ifstream f_file("shader.frag");

	 std::string v_str((std::istreambuf_iterator<char>(v_file)),std::istreambuf_iterator<char>());
	 std::string f_str((std::istreambuf_iterator<char>(f_file)),std::istreambuf_iterator<char>());

	 const char * v_source = v_str.c_str();
	 const char * f_source = f_str.c_str();

	 if ( v_source == NULL ) {
		 printf("Vertex shader not found.\n");
		 exit(1);
	 }
	 if ( f_source == NULL ) {
		 printf("Fragment shader not found.\n");
		 exit(1);
	 }

	 printf("Read shader files.\n");

	 //create shaders
	 GLuint v_shader, f_shader;
	 v_shader = glCreateShader(GL_VERTEX_SHADER);
	 f_shader = glCreateShader(GL_FRAGMENT_SHADER);

	 printf("Created Shaders.\n");

	 //attach to shader
	 glShaderSource(v_shader,1, (const GLchar **)&v_source, NULL );
	 glShaderSource(f_shader,1, (const GLchar **)&f_source, NULL );

	 //free(v_source);
	 //free(f_source);

	 //compile shaders
	 glCompileShader(v_shader);
	 glCompileShader(f_shader);

	 GLint status;
	 glGetShaderiv(v_shader,GL_COMPILE_STATUS, &status);
	 if ( status != GL_TRUE ) {
		 printf("Error in vertex shader compile. \n");
		 exit(1);
	 }
	 glGetShaderiv(f_shader,GL_COMPILE_STATUS, &status);
	 if ( status != GL_TRUE ) {
		 printf("Error in fragment shader compile. \n");
		 int maxLength;
		 int length;
		 glGetShaderiv(f_shader, GL_INFO_LOG_LENGTH,&maxLength);
		 char* log = new char[maxLength];
		 glGetShaderInfoLog(f_shader, maxLength,&length,log);
		 printf(log);
		 exit(1);
	 }

	 int maxLength;
	 int length;
	 glGetShaderiv(f_shader, GL_INFO_LOG_LENGTH,&maxLength);
	 char* log = new char[maxLength];
	 glGetShaderInfoLog(f_shader, maxLength,&length,log);
	 printf(log);

	 printf("Compiled Shaders.\n");

	 //create program

	 program = glCreateProgram();
	 glAttachShader(program, v_shader);
	 glAttachShader(program, f_shader);
	 glLinkProgram(program);

	 glGetProgramiv(program, GL_LINK_STATUS, &status);
	 if ( status != GL_TRUE ) {
		 printf("Error in shader link: %i \n", status);

		 int maxLength;
		 int length;
		 glGetProgramiv(program,GL_INFO_LOG_LENGTH,&maxLength);
		 char* log = new char[maxLength];
		 glGetProgramInfoLog(program,maxLength,&length,log);
		 printf(log);

		 exit(1);
	 }

	 printf("Create program.\n");

	 glUseProgram(program);

	 printf("Use program.\n");
 }

 GLint * createFilter() {
	 char filter_data[] = {
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)-8,(char)-8,	(char)-8,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1

			 //(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,

	 };
	 /*char filter_data[] = {
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)-8,(char)-8,(char)-8, 	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,
			 (char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1,	(char)1,(char)1,(char)1
	 };*/

	 generateTexture(9,9,filter_data,1);
	 GLint returned[2] = {9,9};
	 return returned;
 }

 void init()
 {
     glClearColor(0.0, 0.0, 0.0, 0.0);
     glShadeModel(GL_SMOOTH);

     glEnable(GL_LIGHTING);
     glEnable(GL_LIGHT0);
     glEnable(GL_DEPTH_TEST);

     //glLightfv(GL_LIGHT0, GL_POSITION, (GLfloat[]){2.0, 2.0, 2.0, 0.0});
     //glLightfv(GL_LIGHT0, GL_AMBIENT, (GLfloat[]){1.0, 1.0, 1.0, 0.0});
     initTexture(2);
     loadTexture("image.png", 256, 256);
     GLint * filter_size;
     filter_size = createFilter();
     printf("filter size = %i,%i\n",filter_size[0], filter_size[1]);

     if ( shader_on == true ) {
		 initShaders();

		 GLint units;
		 glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS,&units);
		 printf("Max Texture Units: %i. \n", units);

		 GLint texloc;
		 texloc = glGetUniformLocation(program, "textureID");
		 glUniform1i(texloc, 0);

		 texloc = glGetUniformLocation(program, "filterID");
		 glUniform1i(texloc, 1);

		 GLint loc = glGetUniformLocation(program, "image_width");
		 glUniform1i( loc, width);
		 loc = glGetUniformLocation(program, "image_height");
		 glUniform1i( loc, height);
		 loc = glGetUniformLocation(program, "filter_width");
		 glUniform1i( loc, 9);
		 loc = glGetUniformLocation(program, "filter_width");
		 glUniform1i( loc, 9);
     }
 }

 void resize(int w, int h)
 {
     glViewport(0, 0, (GLsizei) w, (GLsizei) h);
 }

 void key(unsigned char key, int x, int y)
 {
     if (key == 'q') exit(0);
 }

int main(int argc, char *argv[]) {

	std::ifstream in("image.png");


	in.seekg(16);
	in.read((char *)&width, 4);
	in.read((char *)&height, 4);

	width = ntohl(width);
	height = ntohl(height);
	std::cout <<  "image is " << width << " pixels wide and " << height << " pixels high.\n";



	//std:cout << "OpenGL Version: " << glGetString(GL_VERSION) << "\n";
	//std:cout << "GLSL Version: " << glGetString(GL_SHADING_LANGUAGE_VERSION) << "\n";



	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGBA | GLUT_DEPTH);
	glutInitWindowSize(width, height);
	glutCreateWindow("Texture demo - [q]uit");

	printf("OpenGL Version: %s \n", (char*)glGetString(GL_VERSION));
	printf("GLSL Version: %s \n", (char*)glGetString(GL_SHADING_LANGUAGE_VERSION));

	glewInit();
	init();
	glutDisplayFunc(render);
	glutReshapeFunc(resize);
	glutKeyboardFunc(key);




	glutMainLoop();

	return 0;
}

