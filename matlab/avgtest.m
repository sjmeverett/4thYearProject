function [ err ] = avgtest( x, y, hiddenNodes, iterations, cases )
    results = zeros(cases, 1);

    for i = 1:cases
        [th1, th2] = nntrain(x, y, hiddenNodes, iterations);
        p = nnpredict(th1, th2, x);
        results(i) = confusion(y', p');
    end
    
    err = mean(results);
end

