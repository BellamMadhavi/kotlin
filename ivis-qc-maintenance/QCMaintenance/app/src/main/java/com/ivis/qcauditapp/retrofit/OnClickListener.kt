package com.ivis.qcauditapp.retrofit


interface OnClickListener {
    fun onAudioItemClick(position: Int,status: Boolean )
    fun TwowayItemClick(status: Boolean)
    fun SensorItemClick(position: Int )
    fun PlayAudioApi(position:   Int )
}