l = input ()
while l != -1:
	numbers = []
	for i in range (l):
		numbers.append (input ())
	
	numbers.sort ()
	
	for i in numbers:
		print i
	
	print ""
	l = input ()
	