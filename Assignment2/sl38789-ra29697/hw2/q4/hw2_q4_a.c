#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <string.h>

void MatrixMult(char file1[],char file2[],int T)
{
    double start = omp_get_wtime();

	int matrix1Row, matrix2Row, matrix1Col, matrix2Col;

	FILE *fp1 = fopen(file1, "r");
	fscanf(fp1, "%d %d", &matrix1Row, &matrix1Col);
	double** matrix1 = malloc(matrix1Row*sizeof(double*));
	for(int i = 0; i < matrix1Row; i++) {
		matrix1[i] = malloc(matrix1Col*sizeof(double));
	}

    double element = 0;
    int row = 0, col = 0;
    while(fscanf(fp1, "%lf", &element) != EOF) {
        matrix1[row][col] = element;
        col++;
        if(col == matrix1Col) {
            row++;
            col = 0;
        }
    }
    fclose(fp1);

    FILE *fp2 = fopen(file2, "r");
	fscanf(fp2, "%d %d", &matrix2Row, &matrix2Col);
	double** matrix2 = malloc(matrix2Row*sizeof(double*));
	for(int i = 0; i < matrix2Row; i++) {
		matrix2[i] = malloc(matrix2Col*sizeof(double));
	}
    element = 0;
    row = 0, col = 0;

    while(fscanf(fp2, "%lf", &element) != EOF) {
        matrix2[row][col] = element;
        col++;
        if(col == matrix2Col) {
            row++;
            col = 0;
        }
    }
    fclose(fp2);

	double** finalOutput = malloc(matrix1Row*sizeof(double*));
	for(int i = 0; i < matrix1Row; i++) {
		finalOutput[i] = malloc(matrix2Col*sizeof(double));
	}

	#pragma omp parallel num_threads(T)
	{
        int total, iterator, row_, col_;
        for(row_ = 0; row_ < matrix1Row; row_++) {
            for(col_ = 0; col_ < matrix2Col; col_++) {
                total = 0;
                for(iterator = 0; iterator < matrix1Col; iterator++) {
                    total += matrix1[row_][iterator]*matrix2[iterator][col_];
                }
                finalOutput[row_][col_] = total;
            }
        }
	} 
printf("%d ", matrix1Row);
printf("%d", matrix2Col);
printf("\n");
for (int rows=0; rows<matrix1Row; rows++)
{
    for(int columns=0; columns<matrix2Col; columns++)
        {
         printf("%lf  ", finalOutput[rows][columns]);
        }
    printf("\n");
 }
 free(matrix1);
 free(matrix2);
 free(finalOutput);
}

int main(int argc, char *argv[])
{
  char *file1, *file2;
  file1=argv[1];
  file2=argv[2];
  int T=atoi(argv[3]);  
  MatrixMult(file1,file2,T);
}
