

#x = sin(2*pi*1:100/50) + 1#sinusoid
#w = cos(2*pi*1:100/50) + 3#cosenosoid

x = sin(2*pi*1:250/50) #sinusoid
w = cos(2*pi*1:250/50) #cosenosoid

plot.ts(c(x,w))
write.csv(c(x,w), file = "example.csv", row.names = F, col.names = F)