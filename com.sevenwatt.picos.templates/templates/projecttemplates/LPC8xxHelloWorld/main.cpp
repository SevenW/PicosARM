// Demonstrate printf over the serial port.
// and blink by periodically sending a message to the serial port.
// Name        : $(baseName).cpp
// Author      : $(author)
// Copyright   : $(copyright)
// See http://jeelabs.org/2014/12/10/dips-into-the-lpc810/
// See http://jeelabs.org/2014/12/03/garage-parking-aid/

#include <stdio.h>
#include "LPC8xx.h"
#include "uart.h"

extern "C" void SysTick_Handler () {
    // the only effect is to generate an interrupt, no work is done here
}

void delay (int millis) {
    while (--millis >= 0)
        __WFI(); // wait for the next SysTick interrupt
}

int main () {
    LPC_SWM->PINASSIGN0 = 0xFFFFFF04UL; // only connect TXD
    uart0Init(115200);
    SysTick_Config(12000000/1000); // 1000 Hz

    // send out a greeting every second
    while (true) {
        printf("$(message)\n", LPC_SYSCON->DEVICE_ID>>4);
        printf("Built for $(LPCType). Running on LPC%03x.\n", LPC_SYSCON->DEVICE_ID>>4);
        delay(1000);
    }

    return 0;
}
