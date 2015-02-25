#CFLAGS += -DSTARTUP_NO_IRQS
ISPOPTS += -term
LINK = {{LPCType}}.ld

OBJS = {{baseName}}.o system_LPC8xx.o gcc_startup_lpc8xx.o \
		uart.o printf.o printf-retarget.o

LIBDIR = common
SHARED = $(LIBDIR)
include $(SHARED)/rules.mk
