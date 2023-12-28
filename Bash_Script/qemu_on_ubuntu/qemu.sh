#!/bin/bash

DISK_IMAGE_FILE=CentOS-7-x86_64.qcow2
DISK_IMAGE_FILE_SIZE=10G
INSTALL_ISO_FILE=CentOS-7-x86_64-Minimal-1908.iso
GATEWAY_ADDR=192.168.1.1
INET_ADDR=192.168.1.100
INET_DEV=enp0s25
NETWORK_ADDR=192.168.1.0/24

BRIDGE_DEV=br0
TAP_DEV=tap0
MEM_SIZE=640
L2CACHE_SIZE=2621440

RETVAL=0

print() {
	echo "DISK_IMAGE_FILE=${DISK_IMAGE_FILE}"
	echo "DISK_IMAGE_FILE_SIZE=${DISK_IMAGE_FILE_SIZE}"
	echo "INSTALL_ISO_FILE=${INSTALL_ISO_FILE}"
	echo "GATEWAY_ADDR=${GATEWAY_ADDR}"
	echo "INET_ADDR=${INET_ADDR}"
	echo "INET_DEV=${INET_DEV}"
	echo "NETWORK_ADDR=${NETWORK_ADDR}"
	
	echo "BRIDGE_DEV=${BRIDGE_DEV}"
	echo "TAP_DEV=${TAP_DEV}"
	echo "MEM_SIZE=${MEM_SIZE}"
	echo "L2CACHE_SIZE=${L2CACHE_SIZE}"
}

qemu() {
	sudo apt-get install qemu-kvm qemu
}

backup() {
#	gzip -9 -k ${DISK_IMAGE_FILE}
	qemu-img convert -O qcow2 ${DISK_IMAGE_FILE} "${DISK_IMAGE_FILE}.1"
	gzip -9 "${DISK_IMAGE_FILE}.1"
}

create() {
	if [ -f "${DISK_IMAGE_FILE}" ]; then
		echo "${DISK_IMAGE_FILE} exist"
	else
		qemu-img create -f qcow2 ${DISK_IMAGE_FILE} ${DISK_IMAGE_FILE_SIZE}
		RETVAL=$?
		return ${RETVAL}
	fi
}

install_with_kernel_image() {
	qemu-system-x86_64 \
		-nographic \
		-hda ${DISK_IMAGE_FILE} \
		-kernel vmlinuz \
		-initrd initrd.img \
		-boot d \
		-cdrom ${INSTALL_ISO_FILE} \
		-m ${MEM_SIZE} \
		-net nic \
		-net user \
		-append "console=tty0 console=ttyS0,115200n8 edd=off"
	RETVAL=$?
	return ${RETVAL}
}

install() {
	qemu-system-x86_64 \
		-nographic \
		-hda ${DISK_IMAGE_FILE} \
		-boot d \
		-cdrom ${INSTALL_ISO_FILE} \
		-m ${MEM_SIZE} \
		-net nic -net user
	RETVAL=$?
	return ${RETVAL}
}

boot() {
	qemu-system-x86_64 \
		-nographic \
		-m ${MEM_SIZE} \
		-net nic \
		-net tap,ifname=${TAP_DEV},script=no \
		-drive file=${DISK_IMAGE_FILE},l2-cache-size=${L2CACHE_SIZE},media=disk \
		-boot order=c \
		-vga none
	RETVAL=$?
	return ${RETVAL}
}

tap_with_zeroconf() {
	sudo modprobe tun tap && \
	sudo ip link add ${BRIDGE_DEV} type bridge && \
	sudo ip tuntap add dev ${TAP_DEV} mode tap && \
	sudo ip link set dev ${TAP_DEV} master ${BRIDGE_DEV} && \
	sudo ip link set dev ${INET_DEV} master ${BRIDGE_DEV} && \
	sudo ip link set dev ${BRIDGE_DEV} up && \
	sudo ip link set dev ${TAP_DEV} up && \
	sudo ip route flush dev ${INET_DEV} && \
	sudo ip address delete ${INET_ADDR} dev ${INET_DEV} && \
	sudo ip address add ${INET_ADDR} dev ${BRIDGE_DEV} && \
	sudo ip route add ${NETWORK_ADDR} dev ${BRIDGE_DEV} && \
	sudo ip route add 169.254.0.0/16 dev ${BRIDGE_DEV} && \
	sudo ip route add 0.0.0.0/0 via ${GATEWAY_ADDR}
}

untap_with_zeroconf() {
	sudo ip route flush dev ${BRIDGE_DEV} && \
	sudo ip address delete ${INET_ADDR} dev ${BRIDGE_DEV} && \
	sudo ip address add ${INET_ADDR} dev ${INET_DEV} && \
	sudo ip route flush dev ${INET_DEV} && \
	sudo ip route add ${NETWORK_ADDR} dev ${INET_DEV} && \
	sudo ip route add 169.254.0.0/16 dev ${INET_DEV} && \
	sudo ip route add 0.0.0.0/0 via ${GATEWAY_ADDR}
	sudo ip link set dev ${TAP_DEV} down && \
	sudo ip link set dev ${BRIDGE_DEV} down && \
	sudo ip link set dev ${INET_DEV} nomaster && \
	sudo ip link set dev ${TAP_DEV} nomaster && \
	sudo ip tuntap delete dev ${TAP_DEV} mode tap && \
	sudo ip link delete ${BRIDGE_DEV} type bridge
}

tap() {
	sudo modprobe tun tap && \
	sudo ip link add ${BRIDGE_DEV} type bridge && \
	sudo ip tuntap add dev ${TAP_DEV} mode tap && \
	sudo ip link set dev ${TAP_DEV} master ${BRIDGE_DEV} && \
	sudo ip link set dev ${INET_DEV} master ${BRIDGE_DEV} && \
	sudo ip link set dev ${BRIDGE_DEV} up && \
	sudo ip link set dev ${TAP_DEV} up && \
	sudo ip route flush dev ${INET_DEV} && \
	sudo ip address delete ${INET_ADDR} dev ${INET_DEV} && \
	sudo ip address add ${INET_ADDR} dev ${BRIDGE_DEV} && \
	sudo ip route add ${NETWORK_ADDR} dev ${BRIDGE_DEV} && \
	sudo ip route add 0.0.0.0/0 via ${GATEWAY_ADDR}
}

untap() {
	sudo ip route flush dev ${BRIDGE_DEV} && \
	sudo ip address delete ${INET_ADDR} dev ${BRIDGE_DEV} && \
	sudo ip address add ${INET_ADDR} dev ${INET_DEV} && \
	sudo ip route flush dev ${INET_DEV} && \
	sudo ip route add ${NETWORK_ADDR} dev ${INET_DEV} && \
	sudo ip route add 0.0.0.0/0 via ${GATEWAY_ADDR} && \
	sudo ip link set dev ${TAP_DEV} down && \
	sudo ip link set dev ${BRIDGE_DEV} down && \
	sudo ip link set dev ${INET_DEV} nomaster && \
	sudo ip link set dev ${TAP_DEV} nomaster && \
	sudo ip tuntap delete dev ${TAP_DEV} mode tap && \
	sudo ip link delete ${BRIDGE_DEV} type bridge
}

case "$1" in
qemu)
	qemu
	;;
backup)
	backup
	;;
start)
	boot
	;;
boot)
	boot
	;;
install)
	install
	;;
create)
	create
	;;
tap)
	tap
	;;
untap)
	untap
	;;
print)
	print
	;;
*)
	echo $"Usage: $0 {print|qemu|backup|start|boot|install|create|tap|untap}"
esac

exit ${RETVAL}
