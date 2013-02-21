function [ th1, th2 ] = nntrain( x, y, hiddenNodes, iterations )
    inputNodes = size(x, 2);
    outputNodes = size(y, 2);
    
    th1 = rand(hiddenNodes, inputNodes + 1);
    th2 = rand(outputNodes, hiddenNodes + 1);
    
    beta = 1;

    for i = 1:iterations
        for j = 1:size(x, 1)
            a1 = [1; x(j,:)'];
            a2 = [1; sig(th1 * a1)];
            a3 = sig(th2 * a2);

            t = y(j,:)';
            d3 = (t - a3) .* sigd(a3);
            d2 = (th2(:,2:end)' * d3) .* sigd(a2(2:end));

            th1 = th1 + beta * d2 * a1';
            th2 = th2 + beta * d3 * a2';
        end

        if (mod(i, 200) == 0)
            d = t - a3;
            e = d' * d;
            disp(e)
        end
    end
end

