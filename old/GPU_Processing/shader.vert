#version 120
varying vec2 vTexCoord;
void main ()
{
	gl_TexCoord[0] = gl_MultiTexCoord0;
	vTexCoord = gl_Vertex.xy;
	gl_Position = ftransform ();
}