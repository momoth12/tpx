#include <iostream>
#include <string>
#include <vector>
#include <cmath>
#include <numeric>

#include "kernel.hpp"
#include "confusion_matrix.hpp"
#include "svm.hpp"

SVM::SVM(Dataset* dataset, int col_class, Kernel K):
    col_class(col_class), kernel(K) {
    train_labels = std::vector<int>(dataset->GetNbrSamples());
    train_features = std::vector<std::vector<double>>(dataset->GetNbrSamples(), std::vector<double>(dataset->GetDim() - 1));
    // Exercise 2: put the correct columns of dataset in train_labels and _features AGAIN!
    // BEWARE: transform 0/1 labels to -1/1
    for(int i=0; i <dataset->GetNbrSamples() ;i++){
        if(dataset->GetInstance(i)[col_class]==0){
            train_labels[i]=-1;
        }
        else{
            train_labels[i]=1;
        }
    }
    for(int i=0;i<dataset->GetNbrSamples();i++){
        for (int j = 0; j < dataset->GetDim(); j++)
        {
            if(j<col_class){
                train_features[i][j]=dataset->GetInstance(i)[j];
            }
            else if (j>=col_class){
                train_features[i][j]=dataset->GetInstance(i)[j+1];
            }
        }
        
    }
///////////
    compute_kernel();
}

SVM::~SVM() {
}

void SVM::compute_kernel() {
    const int n = train_features.size();
    alpha = std::vector<double>(n);
    computed_kernel = std::vector<std::vector<double>>(n, std::vector<double>(n));
    int i=0;
    int j=0;

    // Exercise 2: put y_i y_j k(x_i, x_j) in the (i, j)th coordinate of computed_kernel
    for(int i=0;i<n;i++){
        for(int j=0;j<n;j++){
            computed_kernel[i][j]=this->kernel.k(train_features[i],train_features[j])*train_labels[i]*train_labels[j];
        }
    }
    
    
}

void SVM::compute_beta_0(double C) {
    // count keeps track of the number of support vectors (denoted by n_s)
    int count = 0;
    beta_0 = 0.0;
    int n=alpha.size();
    // Exercise 3
    // Use clipping_epsilon < alpha < C - clipping_epsilon instead of 0 < alpha < C
    // This performs 1/n_s
    for(int i=0;i<n;i++){
        if(clipping_epsilon<alpha[i] && C-clipping_epsilon>alpha[i]){
            double dd=0.0;
            count +=1;
            for(int j=0;j<n;j++){
                dd+=alpha[j]*train_labels[j]*kernel.k(train_features[j],train_features[i]);
            }
            dd-=train_labels[i];
            beta_0+=dd;
        }
        
    }
    
    beta_0 /= count;
}

void SVM::train(const double C, const double lr) {
    // Exercise 4
    // Perform projected gradient ascent
    // While some alpha is not clipped AND its gradient is above stopping_criterion
    // (1) Set stop = false
    // (2) While not stop do
    // (2.1) Set stop = true
    // (2.2) For i = 1 to n do
    // (2.2.1) Compute the gradient - HINT: make good use of computed_kernel
    // (2.2.2) Move alpha in the direction of the gradient - eta corresponds to lr (for learning rate)
    // (2.2.3) Project alpha in the constraint box by clipping it
    // (2.2.4) Adjust stop if necessary
    // (3) Compute beta_0

    bool stop=false;
    while (!stop){
        
        stop=true;
        for(int i=0;i<train_labels.size();i++){
            double grad=1.0;
            bool clipping=false;
            for(int j=0;j<(alpha.end()-alpha.begin());j++){
                grad-=alpha[j]*computed_kernel[i][j];
            }
            alpha[i]+=lr*grad;
            if(alpha[i]>0 && alpha[i]<C){
                if(std::abs(grad)>stopping_criterion){
                    stop=false;
                }
            }
            else{
                alpha[i]=std::max(0.0,std::min(alpha[i],C));
            }
            
        }

    }


    // Update beta_0
    compute_beta_0(C);
}

int SVM::f_hat(const std::vector<double> x) {
    // Exercise 5
    int n =train_labels.size();
    double pred=-beta_0;
    for(int i=0;i<n;i++){
        pred+=alpha[i]*train_labels[i]*kernel.k(train_features[i],x);
    }
    if(pred<0.0) return -1;
    else return 1;
}

ConfusionMatrix SVM::test(const Dataset* test) {
    // Collect test_features and test_labels and compute confusion matrix
    std::vector<double> test_labels (test->GetNbrSamples());
    std::vector<std::vector<double>> test_features (test->GetNbrSamples(), std::vector<double>(test->GetDim() - 1));
    ConfusionMatrix cm;

    // Exercise 6
    // Put test dataset in features and labels
    // Use f_hat to predict and put into the confusion matrix
    // BEWARE: transform -1/1 prediction to 0/1 label

    return cm;
}

int SVM::get_col_class() const {
    return col_class;
}

Kernel SVM::get_kernel() const {
    return kernel;
}

std::vector<int> SVM::get_train_labels() const {
    return train_labels;
}

std::vector<std::vector<double>> SVM::get_train_features() const {
    return train_features;
}

std::vector<std::vector<double>> SVM::get_computed_kernel() const {
    return computed_kernel;
}

std::vector<double> SVM::get_alphas() const {
    return alpha;
}

double SVM::get_beta_0() const {
    return beta_0;
}

void SVM::set_alphas(std::vector<double> alpha) {
    this->alpha = alpha;
}
