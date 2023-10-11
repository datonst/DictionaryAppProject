' Set args = Wscript.Arguments
' Set voice = CreateObject("SAPI.SpVoice")
' voice.Rate = 1
' voice.Volume = 90
' Say = args(0)
' If (Len(Say) > 0) Then
'     voice.Speak Say
' End If 
Set args = Wscript.Arguments
Set voice = CreateObject("SAPI.SpVoice")
voice.Rate = 1
voice.Volume = 90

' Lặp qua danh sách giọng và chọn giọng cụ thể
For Each objVoice in voice.GetVoices
    If objVoice.GetDescription = "Microsoft Hazel Desktop - English (United States)" Then
        Set voice.Voice = objVoice
        Exit For
    End If
Next

Say = args(0)
If (Len(Say) > 0) Then
    voice.Speak Say
End If
