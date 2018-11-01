#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define RAD 10000

double MonteCarlo(int s)
{
	double count, x, y = 0;
	int T = 3;
	int points = s/T;
	#pragma omp parallel num_threads(T)
	{
		for(int i = 0; i < points; i++) {
			x = (RAD) * (double)rand() / (double)RAND_MAX;
			y = (RAD) * (double)rand() / (double)RAND_MAX;
			double value = ((x*x)+(y*y));
			if (value < (RAD*RAD))
				#pragma omp atomic
				count++;
		}
}

	return (4*(count/s));
//Write your code here
}

void main()
{
double pi;
pi=MonteCarlo(100000000);
printf("Value of pi is: %lf\n",pi);
}



