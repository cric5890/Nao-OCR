__kernel void add_floats(__global const float* a, __global const float* b, __global float* out, int n) 
{
    int i = get_global_id(0);
    if (i >= n)
        return;

    out[i] = a[i] + b[i];
}

__kernel void fill_in_values(__global float* a, __global float* b, int n) 
{
    int i = get_global_id(0);
    if (i >= n)
        return;

    a[i] = cos((float)i);
    b[i] = sin((float)i);
}

__kernel void high_pass(__global int* color_out, __global int* filter, int image_size, int filter_width, int image_width, __global int* orginal_color) {
	int center = get_global_id(0);
	if (center >= image_size ) {
		return;
	}
	int start = center - ( ( filter_width - 1 ) / 2 ) *image_width -  ( ( filter_width - 1 ) / 2 );
	if ( start < 0 ) {
		return;
	}else if ( filter_width + start + filter_width*image_width > image_size ) {
		return;
	}
	int y_filter = 0;
	int x_filter = 0;
	int sum = 0;
	for ( int x = start; x < filter_width + start; x++ ) {
		y_filter = 0;
		for ( int y = 0; y < filter_width*image_width; y+= image_width ) {
			sum += orginal_color[x+y]*filter[x_filter + y_filter];
			y_filter += filter_width;
		}
		x_filter++;
	}
	int temp = sum;
	if ( temp < 0 ) {
		temp = 0;
	}
	
	if ( temp > 255) {
		temp = 255;
	}
	
	color_out[center] = temp;
}






