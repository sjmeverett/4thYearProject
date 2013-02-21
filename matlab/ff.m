function [ output ] = ff( th1, th2, input )
    a1 = [1; input'];
    a2 = [1; sig(th1 * a1)];
    output = sig(th2 * a2);
end

