#version 120

uniform sampler2D textureID;
uniform sampler2D filterID;

varying vec2 vTexCoord;

uniform int image_width;
uniform int image_height;
uniform int filter_width;
uniform int filter_height;

void main() {
  
	
	if ( gl_TexCoord[0].x*image_width > 1 && gl_TexCoord[0].y*image_height > 1 && gl_TexCoord[0].x*image_width +1 < image_width && gl_TexCoord[0].y*image_height + 1 < image_height ) {
	
	vec4 sample[9];
	vec2 offset[9];
	offset[0] = vec2(-1.0f/image_width,-1.0f/image_height);
	offset[1] = vec2( 0.0f/image_width,-1.0f/image_height);
	offset[2] = vec2( 1.0f/image_width,-1.0f/image_height);
	offset[3] = vec2(-1.0f/image_width, 0.0f/image_height);
	offset[4] = vec2( 0.0f/image_width, 0.0f/image_height);
	offset[5] = vec2( 1.0f/image_width, 0.0f/image_height);
	offset[6] = vec2(-1.0f/image_width, 1.0f/image_height);
	offset[7] = vec2( 0.0f/image_width, 1.0f/image_height);
	offset[8] = vec2( 1.0f/image_width, 1.0f/image_height);
	for (int i = 0; i < 9; i++) {
		sample[i] = texture2D(textureID, gl_TexCoord[0].xy + offset[i].xy);
		float gray = dot(sample[i].rgb, vec3(0.299f, 0.587f, 0.114f));
        sample[i] = vec4(gray, gray, gray, 1.0 );
    }
	
	
	//vec4 sum = sample[4];
	
	vec4 sum = (sample[4] * 8.0f ) - 
                    (sample[0] + sample[1] + sample[2] + 
                     sample[3] + sample[5] + 
                     sample[6] + sample[7] + sample[8] ) ;
	
	
	for ( int i = 0; i < 3; i++ ) {
		if ( sum[i] < 0 ) {
			sum[i] = 0;
		} else if ( sum[i] > 1 ) {
			sum[i] = 1;
		}		
	}
	//sum[3] = 1;

	gl_FragColor = sum;
	} else {
		//vec4 color = texture2D(textureID, gl_TexCoord[0].xy);
		gl_FragColor = vec4(1,1,1,1);
	}
}

