package gr.elections.sad;

import gr.elections.sad.model.Data;
import gr.elections.sad.model.Device;
import gr.elections.sad.model.RepresentativeInfo;
import gr.elections.sad.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Random;

@SpringBootApplication
@RestController
public class SadApplication
{
    @Autowired
    private DeviceRepository deviceRepository;

    public static void main(String[] args)
    {
        SpringApplication.run(SadApplication.class, args);
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity register(@RequestBody String code, HttpServletResponse response)
    {
        Optional<Device> device = deviceRepository.findById(code);
        if(device.isPresent())
        {
            Device dev = device.get();
            if(dev.getCodeTries() >= 3)
            {
                dev.setCodeTries(dev.getCodeTries()+1);
                deviceRepository.save(dev);
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Device dev = new Device(code);
        deviceRepository.save(dev);

        Cookie cookie = new Cookie("code", code);
        response.addCookie(cookie);

        return new ResponseEntity<>(new RepresentativeInfo(), HttpStatus.OK);
    }

    @GetMapping("/authme")
    public ResponseEntity authme(@CookieValue(value = "id", defaultValue = "none") String code)
    {
        if (!code.equalsIgnoreCase("none"))
        {
            Optional<Device> device = deviceRepository.findById(code);
            if (device.isPresent())
            {
                Device dev = device.get();
                Random r = new Random();
                String pass = String.format("%04d", r.nextInt(10000));
                dev.setPass(DigestUtils.sha1Hex(pass));
                deviceRepository.save(dev);

                return new ResponseEntity(pass, HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody String pass, @CookieValue(value = "id", defaultValue = "none") String code)
    {
        if (!code.equalsIgnoreCase("none"))
        {
            Optional<Device> device = deviceRepository.findById(code);
            if (device.isPresent())
            {
                Device dev = device.get();
                if(dev.getPassCount() < 3)
                {
                    dev.setPassCount(dev.getPassCount() + 1);
                    if (dev.getPass().equalsIgnoreCase(DigestUtils.sha1Hex(pass)))
                    {
                        dev.setAuthed((short) 54);
                        deviceRepository.save(dev);
                        return new ResponseEntity(HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/data")
    public ResponseEntity data(@RequestBody String data, @CookieValue(value = "id", defaultValue = "none") String code)
    {
        if (!code.equalsIgnoreCase("none"))
        {
            Optional<Device> device = deviceRepository.findById(code);
            if (device.isPresent())
            {
                Device dev = device.get();
                if(dev.getAuthed() == 54)
                {
                    dev.setData(new Data(data));
                    dev.setAuthed((short) 8);
                    deviceRepository.save(dev);
                    return new ResponseEntity(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
