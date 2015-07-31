-- | CS240h Lab 2 Chat Server
module Chat (chat) where


--import System-Console-ANSI
import Network.Socket.Internal
import Data.Word
import Data.List
import Control.Concurrent
import Control.Exception
import Network
import System.IO
import System.Environment


broadcast :: [(Int,Handle)] -> String -> IO ()
broadcast [] m = return ()
broadcast ((_,h):hs) m = do 
                     hPutStrLn h m
                     broadcast hs m


talk :: (Int,Handle) -> MVar [(Int,Handle)] -> IO ()
talk (x,h) handles = do
                 resp <- hGetLine h
                 if resp == "quit" then return () else do
                   tmp <- readMVar handles
                   broadcast tmp $ (show x) ++ ": " ++ resp
                   talk (x,h) handles 



forker :: Socket -> MVar [(Int,Handle)] -> MVar Int -> IO ThreadId
forker s handles cntr = forkIO $ bracket (accept s) (\(h,_,_) -> hClose h) $
             \(h,_,_) -> do 

               --add this client to the list of handles
               tmpCntr <- readMVar cntr
               swapMVar cntr $ tmpCntr + 1

               tmp <- readMVar handles
               swapMVar handles $ tmp ++ [(tmpCntr,h)]
               
               hPutStrLn h "Enter 'quit' to leave chatroom"

               --broadcast client's entry to the chatroom
               broadcast tmp $ (show tmpCntr) ++ " has joined" 
               putStrLn $ (show tmpCntr) ++ " has joined, acquiring lock"

               --branch so more clients can join
               forker s handles cntr

               --allow chat interface until user exits
               talk (tmpCntr,h) handles
               
               --remove client from list of handles
               oldHandle <- readMVar handles
               swapMVar handles $ delete (tmpCntr,h) oldHandle
               
               --broadcast client's exit to the chatroom
               broadcast (delete (tmpCntr,h) oldHandle) $ (show tmpCntr) ++ " has left"
               putStrLn $ (show tmpCntr) ++ " has exited, lock released"



listen :: PortID -> IO ()
listen listenPort = bracket (listenOn listenPort) sClose $ \s -> do
                      handles <- newMVar []
                      cntr <- newMVar 1
                      forker s handles cntr
                      getLine
                      return ()


-- | Chat server entry point.
chat :: IO ()
chat = do
       port <- getEnv "CHAT_SERVER_PORT"
       putStrLn port
       --let x = fromIntegral (read port :: Int) 
       --  in listen $ PortNumber $ PortNum x
       listen (PortNumber 1237)
       return ()


