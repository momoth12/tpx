#include "perceptron.hpp"
#include "activation.hpp"
#include <iostream>
#include <assert.h>
#include <algorithm> // std::max, std::min
#include <cmath>

OneLayerPerceptron::OneLayerPerceptron(int _dim, int _size, double _rate, double _decay,
                                       std::function<double(double)> _activation,
                                       std::function<double(double)> _activation_der)
    : dim(_dim), size(_size), inputs(dim, nullptr), hidden(size, nullptr),
      rate(_rate), decay(_decay)
{
    // To complete this constructor, have a look at those
    // of the classes Node and Neuron

    // Recall:
    // dim - the dimension of the input (excluding already the regression column)
    // size - the number of neurons in the hidden layer of the perceptron

    // TODO Exercise 3: Create nodes for the inputs

    // TODO Exercise 3: Create neurons for the hidden layer connecting them to the inputs

    // TODO Exercise 3: Create the output neuron connecting it to those of the hidden layer
    // and using identity (x |--> x) as the activation function

    initLearningRate(_rate);
}

OneLayerPerceptron::~OneLayerPerceptron()
{
    // Complete this destructor
    // (as a rule of thumb, everything that is 
    //  created in a constructor must be deleted here)
    for (auto node : inputs)
    {
        // TODO Exercise 3
    }

    // TODO Exercise 3
}

 /*******************************\
( *          Getters            * )
 \*******************************/

int OneLayerPerceptron::getNbNeurons()
{
    return hidden.size();
}

double OneLayerPerceptron::getLearningRate()
{
    return rate;
}

double OneLayerPerceptron::getDecay()
{
    return decay;
}

 /*******************************\
( *          Setters            * )
 \*******************************/

void OneLayerPerceptron::setLearningRate(double _rate)
{
    rate = _rate;

    for (auto neuron : hidden) {
        assert (neuron != nullptr);
        neuron->setLearningRate(rate);
    }

    assert (output != nullptr);
    output->setLearningRate(rate);
}

void OneLayerPerceptron::initLearningRate(double _rate)
{
    epoch = 0;
    setLearningRate(_rate);
}

void OneLayerPerceptron::decayLearningRate()
{
    epoch++;
    setLearningRate(rate / (1 + decay * epoch));
}

 /*******************************\
( *          Execution          * )
 \*******************************/

const double sqrt3 = 1.73205080757;
const double eps = 0.001;

double OneLayerPerceptron::normalise(double val, Dataset *data, int coord)
{
    // https://stats.stackexchange.com/questions/47590/what-are-good-initial-weights-in-a-neural-network
    // "If the inputs are normalized to have mean 0 and standard deviation 1..."
    assert (0 <= coord && coord < data->getDim());
    if (std::abs(data->getStddev(coord)) < eps ) 
        return val;
    else
        return (val - data->getMean(coord))/data->getStddev(coord);
}

double OneLayerPerceptron::denormalise(double val, Dataset *data, int coord)
{
    assert (0 <= coord && coord < data->getDim());
    return val*data->getStddev(coord) + data->getMean(coord);
}

void OneLayerPerceptron::prepareInputs(Dataset *data, int row, int regr, bool print)
{
    using namespace std;

    // Initialise the input signals, skipping the regression
    // column -- do not forget to normalise the data (see above)

    if (print)
        cout << "Initialising input signals...\t";

    // TODO Exercise 4

    if (print)
        cout << "done." << endl;
}

void OneLayerPerceptron::computeHiddenStep(bool print)
{
    using namespace std;

    if (print)
        cout << "Running internal layer neurons..." << endl;

    // TODO Exercise 4: Compute one step of the hidden layer

    if (print)
        cout << "done." << endl;
}

double OneLayerPerceptron::computeOutputStep(Dataset *data, int row, int regr, bool print)
{
    // This method should comprise both forward and backward propagation
    
    using namespace std;

    double ret, error;

    if (print)
        cout << "Running output neuron..." << endl
             << *output << endl;

    // TODO Exercise 4: Compute one step of the output neuron

    if (print)
        cout << "done. Output = " << ret << endl;

    if (print)
        cout << "Setting up back propagation...\t";

    // Compute the error for back propagation
    error = -2 * (normalise(data->getInstance(row)[regr], data, regr) - ret);
    // TODO Exercise 4: Initialise the back propagation

    if (print)
    {
        cout << "done. Propagated error = " << error << endl;
        cout << "Running back propagation through the output neuron...\t";
    }

    // TODO Exercise 4: Propagate back through the output neuron

    if (print)
        cout << "done." << endl;

    return denormalise(ret, data, regr);
}

void OneLayerPerceptron::propagateBackHidden(bool print)
{
    using namespace std;

    if (print)
        cout << "Running back propagation through the hidden layer...\t";

    // TODO Exercise 4: Propagate back through the hidden layer

    if (print)
        cout << "done." << endl;
}

double OneLayerPerceptron::run(Dataset *data, int row, int regr, bool print)
{
    assert(data->getDim() == dim + 1);
    assert(0 <= regr && regr < data->getDim());

    prepareInputs(data, row, regr, print);

    computeHiddenStep(print);

    double ret = computeOutputStep(data, row, regr, print);

    propagateBackHidden(print);

    return ret;
}
