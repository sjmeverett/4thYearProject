function [ err ] = nntest( file, inputNodes, hiddenNodes, iterations )
    data = csvread(file);
    x = data(:, 1:inputNodes);
    y = data(:, (inputNodes+1):end);
    [th1, th2] = nntrain(x, y, hiddenNodes, iterations);
    p = nnpredict(th1, th2, x);
    err = confusion(y', p');
end
