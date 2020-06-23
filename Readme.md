1. Client və server hissəsinə uyğun olaraq logout funcsionallığı əlavə edin.
 Client tərəfdə console-a exit yazılan zaman serverə metaData-da sessiyanın bitməsinə
 dair məlumat serverə ötürülməlidir. Server öz növbəsinə metaData-dakı bu məlumatı tanımalı və həmin client
 üçün ayrılmış socketi bağlamalıdır. Əlavə olaraq bu client üçün yaradılmış ClientHandler obyekti ClientHandlerRegistry 
 storageindən silinməlidir. 