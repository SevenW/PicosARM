// Demonstrate printf over the serial port.
// and blink by periodically sending a message to the serial port.
// Name        : $(baseName).cpp
// Author      : $(author)
// Copyright   : $(copyright)
// See http://jeelabs.org/2014/12/10/dips-into-the-lpc810/
// See http://jeelabs.org/2014/12/03/garage-parking-aid/

#include "chip.h"
#include "uart.h"
#include <stdio.h>

extern "C" void SysTick_Handler () {
    // the only effect is to generate an interrupt, no work is done here
}

void delay (int millis) {
    while (--millis >= 0)
        __WFI(); // wait for the next SysTick interrupt
}

int main () {
    LPC_SWM->PINASSIGN[0] = 0xFFFFFF04UL; // only connect TXD
    uart0Init(115200);
    SysTick_Config(12000000/1000); // 1000 Hz

    // send out a greeting every second
    while (true) {
        printf("$(message)\n");
        printf("Built for $(LPCType). Running on LPC%03x.\n", (unsigned int)LPC_SYSCON->DEVICEID>>4);
        delay(1000);
    }

    return 0;
}
