from PIL import Image, ImageTk
from Tkinter import Toplevel, Tk, Label
import sys



# def get_hex(dec):
# 	c = hex(dec)[2:]
# 	if len(c)==1:
# 		c = '0'+c
# 	return c
#
# def get_code(rgb):
# 	code = '#'
# 	code += get_hex(rgb[0])
# 	code += get_hex(rgb[1])
# 	code += get_hex(rgb[2])
# 	return code

class CroppedFrame:

	def place(self,x,y):
		self.x = x
		self.y = y

	def init(self):
		self.window = Toplevel()
		self.window.title('no_shadow')
		self.window.overrideredirect(True)
		self.window.geometry(u'{}x{}+{}+{}'.format(self.w,self.h,self.x,self.y))

		self.l = Label(self.window,image=self.tk_image).place(x=0,y=0,width=self.w,height=self.h)

	def __init__(self, image, x,y,w,h):
		# self.window = Toplevel()
		# self.window.overrideredirect(True)

		# print "Image: "+str(image)

		# self.window.geometry(u'{}x{}+0+0'.format(w,h))

		# print "X: {}  Y: {}  W: {}  H: {}".format(x,y,w,h)

		self.cropped = image.crop((x,y,x+w,y+h))
		self.cropped.load()
		self.tk_image = ImageTk.PhotoImage(self.cropped)

		self.w = w
		self.h = h


if len(sys.argv)==1:
	print "Improper use: python program image-file"
	exit(1)

root = Tk()

img = Image.open(sys.argv[1])

img.load()

size = img.size
print "Image Size: "+str(size)

x = 200
y = 200
offset_x = 50
offset_y = 50
width = 50
height = 50

windows = []

for i in range(size[0]/width):
	for j in range(size[1]/height):
		windows.append(CroppedFrame(img,i*width,j*height,width,height))
		windows[-1].place(x+i*offset_x, y+j*offset_y)

for i in range(len(windows)-1, -1, -1):
	windows[i].init()

root.mainloop()
