function [ output ] = nnpredict( th1, th2, input )
    output = zeros(size(input,1), size(th2, 1));
    
    for i = 1:size(input,1)
        output(i,:) = ff(th1, th2, input(i,:));
    end
end

